package org.openactive.PomReporter.service.impl;

import org.apache.log4j.Logger;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.service.PomService;
import org.openactive.PomReporter.service.SvnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mohadib on 1/24/17.
 */
@Service
public class ProjectUpdateServiceImpl
{
  private final static Logger LOG = Logger.getLogger( ProjectUpdateServiceImpl.class );

  @Autowired
  private ProjectDAO projectDAO;

  @Autowired
  private PomService pomService;

  @Autowired
  private SvnService svnService;

  @Scheduled( fixedRate = 300000)
  public void updateProjects()
  {
    List<Project> projects = projectDAO.findAll();
    for( Project project : projects )
    {
      if( project.getSvnInfo() == null )
      {
        try
        {
          svnService.checkoutProject( project );
        }
        catch (Exception e)
        {
          //could not checkout!
          LOG.error( e.getMessage(), e);
          continue;
        }
      }

      try
      {
        svnService.updateProject(project);
        svnService.getInfo( project );
        svnService.getLogs( project );
      }
      catch (Exception e)
      {
        // could not update project, info, or logs
        LOG.error( e.getMessage(), e);
        continue;
      }

      try
      {
        pomService.parsePom( project );
      }
      catch (Exception e )
      {
        LOG.error( e.getMessage(), e);
      }
    }
  }
}
