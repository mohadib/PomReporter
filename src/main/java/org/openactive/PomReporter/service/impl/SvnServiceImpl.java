package org.openactive.PomReporter.service.impl;

import org.openactive.PomReporter.dao.ProjectSvnInfoDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectSvnInfo;
import org.openactive.PomReporter.domain.SvnCredential;
import org.openactive.PomReporter.service.SvnService;
import org.openactive.PomReporter.svn.*;
import org.openactive.PomReporter.util.EncryptionUtil;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

  @Value("${encryption.secret}")
  private String secret;

  @Value("${encryption.salt}")
  private String salt;

  @Override
  public void checkoutProject(Project project) throws SVNException, IOException, GeneralSecurityException
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
    info = projectSvnInfoDAO.save( info );
  }

  @Override
  public void updateProject(Project project) throws SVNException, IOException, GeneralSecurityException
  {
    UpdateActionHandler handler = new UpdateActionHandler();
    doSvnAction( project, handler );

    ProjectSvnInfo info = project.getSvnInfo();
    info.setLastUpdated( new Date() );
    info.setSvnRevision( handler.getRevision() );
    projectSvnInfoDAO.save( info );
  }

  @Override
  public void getLogs(Project project) throws SVNException, IOException, GeneralSecurityException
  {
    StringBuilder buff = new StringBuilder();
    LogActionHandler handler = new LogActionHandler( le -> {
      String logEntry = String.format( "%s | %s | %s<EOL>\n",  le.getDate(), le.getAuthor(), le.getMessage() );
      buff.append( logEntry );
    });
    doSvnAction(project, handler);
    project.getSvnInfo().setLogEntries( buff.toString() );
    projectSvnInfoDAO.save( project.getSvnInfo() );
  }

  @Override
  public void getInfo(Project project) throws SVNException, IOException, GeneralSecurityException
  {
    InfoActionHandler handler = new InfoActionHandler();
    doSvnAction( project, handler );
    SVNInfo info = handler.getInfo();
    project.getSvnInfo().setProjectUrl( removeUsernameFromUrl( info.getURL().toString(), project.getCredentials() ));
    projectSvnInfoDAO.save( project.getSvnInfo() );
  }

  private String removeUsernameFromUrl( String url, SvnCredential credential )
  {
    String userAndHost = String.format( "%s@%s", credential.getUsername(), credential.getHost() );
    if( url.indexOf( userAndHost ) != -1 )
    {
      return url.replace( userAndHost, credential.getHost() );
    }
    return url;
  }

  private void doSvnAction(Project project, SvnActionHandler handler ) throws SVNException, IOException, GeneralSecurityException
  {
    SvnCredential creds = project.getCredentials();

    String passClear = new EncryptionUtil().decrypt(creds.getPassword(), secret.toCharArray(), salt.getBytes("UTF-8" ) );

    SvnActionContext context = new SvnActionContext();
    context.project = project;
    context.svnProjectDir = new FileUtil().getOrCreateSvnProjectDir(project, baseFilePath, allowedChars);
    context.manager = SVNClientManager.newInstance(null, creds.getUsername(), passClear);

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















