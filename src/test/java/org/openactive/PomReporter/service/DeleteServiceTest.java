package org.openactive.PomReporter.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.ProjectSvnInfoDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.openactive.PomReporter.domain.ProjectSvnInfo;
import org.openactive.PomReporter.domain.SvnCredential;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class DeleteServiceTest
{
  @Autowired
  private ProjectDAO projectDAO;

  @Autowired
  private SvnCredenitalDAO svnCredenitalDAO;

  @Autowired
  private ProjectGroupDAO projectGroupDAO;

  @Autowired
  private DeleteService deleteService;

  @Autowired
  private ProjectSvnInfoDAO projectSvnInfoDAO;

  @Value("${svn.checkoutProject.base.path}")
  private String svnBaseDir;

  @Value("${svn.checkoutProject.base.path.fileName.allowed.chars}")
  private String allowedChars;

  @Autowired
  private DataSource dataSource;

  private Project project;
  private SvnCredential cred;
  private ProjectGroup group;
  private File projectDir;
  private File pom;
  private ProjectSvnInfo info;

  @Before
  public void setup() throws Exception
  {
    cred = new SvnCredential();
    cred.setName("test1");
    cred.setHost("foo.com");
    cred.setUsername("JohnQUser");
    cred.setPassword("letmein");
    cred.setPort(1337);
    cred.setProtocol("http");
    cred = svnCredenitalDAO.save(cred);

    group = new ProjectGroup();
    group.setName("Project Group");
    group.setDefault(true);
    group = projectGroupDAO.save(group);

    project = new Project();
    project.setCredentials(cred);
    project.setName("Test Project");
    project.setPath("/svn/some/project");
    project.setXpathExpression("/project/version/text()");
    project.setProjectGroup(group);
    project = projectDAO.save(project);

    FileUtil util = new FileUtil();
    projectDir = util.getOrCreateSvnProjectDir(project, svnBaseDir, allowedChars);
    pom = new File( projectDir, "pom.xml");
    pom.createNewFile();

    info = new ProjectSvnInfo();
    info.setProjectUrl("http://foo.com/svn/some/project");
    info.setSvnRevision(33);
    info.setFilePath(projectDir.getAbsolutePath());
    info.setCreated(new Date());
    info.setLastUpdated(new Date());
    info.setLogEntries("Logs be here");
    info.setXpathResult("result text");
    info.setProject(project);
    info = projectSvnInfoDAO.save(info);

    //refresh project so it has svnInfo link
    project = projectDAO.findOne(project.getId());

    //refresh projectgroup to have link to projects
    group = projectGroupDAO.findOne(project.getId());
  }

  @After
  public void cleanup() throws Exception
  {
    Connection con = dataSource.getConnection();
    Statement stmnt = con.createStatement();
    stmnt.execute("delete from ProjectSvnInfo");
    stmnt.execute("delete from project");
    stmnt.execute("delete from projectgroup");
    stmnt.execute("delete from SvnCredential");
    stmnt.close();
    con.close();

    try {
      FileUtils.deleteDirectory(new File(svnBaseDir));
    } catch (Exception e){}
  }

  @Test
  public void testDeleteProject() throws Exception
  {
    deleteService.deleteProject(project);
    assertNull( projectDAO.findOne( project.getId()));
    assertFalse(projectDir.exists());

    //svnInfo should be gone..
    assertNull(projectSvnInfoDAO.findOne(info.getId()));

    //cred and project group should still exist
    assertNotNull( svnCredenitalDAO.findOne(cred.getId()));
    assertNotNull( projectGroupDAO.findOne(group.getId()));
  }

  @Test
  public void testDeleteCredential() throws Exception
  {
    deleteService.deleteCredentials(cred);

    //cred should be gone
    assertNull( svnCredenitalDAO.findOne(cred.getId()));

    //any projects with cred should be deleted
    assertNull( projectDAO.findOne( project.getId()));
    assertFalse(projectDir.exists());

    //svn info should be gone
    assertNull(projectSvnInfoDAO.findOne(info.getId()));

    //group should still exist
    assertNotNull( projectGroupDAO.findOne(group.getId()));

    //group should not have any projects
    assertEquals(0, projectGroupDAO.findOne(group.getId()).getProjects().size());
  }

  @Test
  public void testDeleteProjectGroup()
  {
    deleteService.deleteProjectGroup(group);
    assertNull( projectGroupDAO.findOne(group.getId()) );
    assertNotNull( projectDAO.findOne(project.getId()));
    assertNotNull( svnCredenitalDAO.findOne( cred.getId()));
  }
}
