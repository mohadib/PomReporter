package org.openactive.PomReporter.util;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by mohadib on 1/23/17.
 */
@Component
public class LoadData
{
  @Autowired
  private SvnCredenitalDAO svnCredRepo;

  @Autowired
  private ProjectDAO projectRepo;

  @Autowired
  private ProjectGroupDAO projectGroupRepo;

  @EventListener({ContextRefreshedEvent.class})
  public void appStarted()
  {
    SvnCredential cred = new SvnCredential();
    cred.setName("Foo Svn Cred");
    cred.setHost("subversion.foo.com");
    cred.setProtocol("http");
    cred.setPort(80);
    cred.setUsername("dib");
    cred.setPassword("letmein");
    svnCredRepo.save(cred);

    ProjectGroup pg = new ProjectGroup();
    pg.setName("Group 1");
    projectGroupRepo.save(pg);

    Project p1 = new Project();
    p1.setCredentials(svnCredRepo.findByName("Foo Svn Cred"));
    p1.setName("FooBar");
    p1.setPath("/svn/foo");
    p1.setXpathExpression("/foo/bar");
    p1.setProjectGroup(pg);
    projectRepo.save(p1);
  }
}
