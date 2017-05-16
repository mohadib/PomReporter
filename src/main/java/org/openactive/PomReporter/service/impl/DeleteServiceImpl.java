package org.openactive.PomReporter.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.VCSCredenitalDAO;
import org.openactive.PomReporter.domain.*;
import org.openactive.PomReporter.domain.ProjectInfo;
import org.openactive.PomReporter.exceptions.LockTimeoutException;
import org.openactive.PomReporter.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by jdavis on 1/25/17.
 */
@Service
public class DeleteServiceImpl implements DeleteService
{
  private final static Logger LOG = Logger.getLogger(DeleteServiceImpl.class);

  @Autowired
  private ProjectDAO projectDAO;

  @Autowired
  private VCSCredenitalDAO svnCredenitalDAO;

  @Autowired
  private ProjectGroupDAO projectGroupDAO;

  @Autowired
  private LockServiceImpl lockService;

  @Value("${svn.checkoutProject.base.path}")
  private String baseFilePath;

  @Value("${entity.delete.lock.timeout:90}")
  private long lockTimeout;

  @Override
  public void deleteProject(Project project) throws Exception
  {
    Lock pomRunLock = lockService.getPomServiceRunLock();
    if (pomRunLock.tryLock(lockTimeout, TimeUnit.SECONDS))
    {
      try
      {
        deleteFiles(project);

        //delete project and child entities
        projectDAO.delete(project);
      }
      finally
      {
        pomRunLock.unlock();
      }
    }
    else
    {
      LOG.info("Could not acquire lock");
      throw new LockTimeoutException("Could not acquire lock");
    }
  }

  @Override
  public void deleteProjectGroup(ProjectGroup projectGroup) throws Exception
  {
    // remove group from projects
    Lock pomRunLock = lockService.getPomServiceRunLock();
    if (pomRunLock.tryLock(lockTimeout, TimeUnit.SECONDS))
    {
      try
      {
        for (Project project : projectGroup.getProjects())
        {
          project.setProjectGroup(null);
          projectDAO.save(project);
        }

        projectGroupDAO.delete(projectGroup);
      }
      finally
      {
        pomRunLock.unlock();
      }
    }
    else
    {
      LOG.info("Could not acquire lock");
      throw new LockTimeoutException("Could not acquire lock");
    }
  }

  @Override
  public void deleteCredentials(VCSCredential credential) throws Exception
  {
    // cascading delete for projects only works if they are eagerly loaded
    credential = svnCredenitalDAO.findByIdAndFetchProjectsEagerly(credential.getId());

    Lock pomRunLock = lockService.getPomServiceRunLock();
    if (pomRunLock.tryLock(lockTimeout, TimeUnit.SECONDS))
    {
      try
      {
        List<Project> projects = credential.getProjects();
        for (Project project : projects)
        {
          deleteFiles(project);
        }

        svnCredenitalDAO.delete(credential);

      }
      finally
      {
        pomRunLock.unlock();
      }
    }
    else
    {
      LOG.info("Could not acquire lock");
      throw new LockTimeoutException("Could not acquire lock");
    }
  }

  private void deleteFiles(Project project) throws Exception
  {
    //delete project from file system
    ProjectInfo svnInfo = project.getProjectInfo();
    if( svnInfo == null )
    {
      // this project has no svnInfo. Most likely it was never
      // checked out for some reason. Log and return
      LOG.info("Project has no svnInfo: " + project.getName());
      return;
    }
    File svnProjectDir = new File(project.getProjectInfo().getFilePath());
    File parent = svnProjectDir.getParentFile();
    File base = new File(baseFilePath);

    if (parent.getParentFile().equals(base))
    {
      FileUtils.deleteDirectory(parent);
    }
    else
    {
      LOG.error("TRYING TO DELETE FILE NOT IN BASE " + parent.getAbsolutePath());
      throw new IllegalArgumentException(parent.getAbsolutePath());
    }
  }
}
