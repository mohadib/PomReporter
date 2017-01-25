package org.openactive.PomReporter.service.impl;

import org.openactive.PomReporter.dao.ProjectSvnInfoDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectSvnInfo;
import org.openactive.PomReporter.domain.SvnCredential;
import org.openactive.PomReporter.service.SvnService;
import org.openactive.PomReporter.svn.*;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;

import java.util.Date;

/**
 * Created by jdavis on 1/24/17.
 */
@Service
public class SvnServiceImpl implements SvnService
{
  @Value("${svn.checkoutProject.base.path.fileName.allowed.chars}")
  private String allowedChars;

  @Value("${svn.checkoutProject.base.path}")
  private String baseFilePath;

  @Autowired
  private ProjectSvnInfoDAO projectSvnInfoDAO;

  @Override
  public void checkoutProject(Project project) throws SVNException
  {
    if ( project.getSvnInfo() != null )
    {
      throw new IllegalArgumentException("Project already has svnInfo? :" + project.getSvnInfo().getFilePath() );
    }

    CreateActionHandler handler = new CreateActionHandler();
    doSvnAction( project,  handler);

    ProjectSvnInfo info = new ProjectSvnInfo();
    info.setProject( project );
    info.setCreated( new Date() );
    info.setFilePath( handler.getSvnProjectDir().getAbsolutePath() );
    projectSvnInfoDAO.save( info );
  }

  @Override
  public void updateProject(Project project) throws SVNException
  {
    UpdateActionHandler handler = new UpdateActionHandler();
    doSvnAction( project, handler );

    ProjectSvnInfo info = project.getSvnInfo();
    info.setLastUpdated( new Date() );
    info.setSvnRevision( handler.getRevision() );
    projectSvnInfoDAO.save( info );
  }

  @Override
  public void getLogs(Project project) throws SVNException
  {
    StringBuilder buff = new StringBuilder();
    LogActionHandler handler = new LogActionHandler( le -> {
      String logEntry = String.format( "%s | %s | %s<EOL>\n",  le.getDate(), le.getAuthor(), le.getMessage() );
      buff.append( logEntry );
    });
    doSvnAction(project, handler);
  }

  @Override
  public void getInfo(Project project) throws SVNException
  {
    InfoActionHandler handler = new InfoActionHandler();
    doSvnAction( project, handler );
    SVNInfo info = handler.getInfo();
    project.getSvnInfo().setProjectUrl( info.getURL().toString() );
    projectSvnInfoDAO.save( project.getSvnInfo() );
  }

  private void doSvnAction(Project project, SvnActionHandler handler ) throws SVNException
  {
    SvnCredential creds = project.getCredentials();

    SvnActionContext context = new SvnActionContext();
    context.project = project;
    context.svnProjectDir = new FileUtil().getOrCreateSvnProjectDir(project, baseFilePath, allowedChars);
    context.manager = SVNClientManager.newInstance(null, creds.getUsername(), creds.getPassword());

    try
    {
      handler.handle(context);
    }
    finally
    {
      context.manager.dispose();
    }
  }
}















