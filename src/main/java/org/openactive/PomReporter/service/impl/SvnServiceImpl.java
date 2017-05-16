package org.openactive.PomReporter.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.openactive.PomReporter.dao.ProjectInfoDAO;
import org.openactive.PomReporter.domain.*;
import org.openactive.PomReporter.domain.ProjectInfo;
import org.openactive.PomReporter.exceptions.VCSProviderException;
import org.openactive.PomReporter.service.VCSProvider;
import org.openactive.PomReporter.svn.*;
import org.openactive.PomReporter.util.EncryptionUtil;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@Service
@Qualifier("svnVcsService")
public class SvnServiceImpl implements VCSProvider
{
	@Value( "${svn.checkoutProject.base.path.fileName.allowed.chars}" )
	private String allowedChars;

	@Value( "${svn.checkoutProject.base.path}" )
	private String baseFilePath;

	@Autowired
	private ProjectInfoDAO projectSvnInfoDAO;

	@Value( "${encryption.secret}" )
	private String secret;

	@Value( "${encryption.salt}" )
	private String salt;

	@Override
	public void checkout( Project project ) throws VCSProviderException
	{
		if ( project.getProjectInfo() != null )
		{
			throw new IllegalArgumentException(
				"Project already has svnInfo? :" + project.getProjectInfo().getFilePath() );
		}

		CreateActionHandler handler = new CreateActionHandler();

		try
		{
			doSvnAction( project, handler );
		}
		catch ( SVNException | IOException | GeneralSecurityException e )
		{
			e.printStackTrace();
			throw new VCSProviderException( e );
		}

		ProjectInfo info = new ProjectInfo();
		info.setProject( project );
		info.setCreated( new Date() );
		info.setFilePath( handler.getSvnProjectDir().getAbsolutePath() );
		info = projectSvnInfoDAO.save( info );
	}

	@Override
	public void update( Project project ) throws VCSProviderException
	{
		UpdateActionHandler handler = new UpdateActionHandler();

		try
		{
			doSvnAction( project, handler );
		}
		catch ( SVNException | IOException | GeneralSecurityException e )
		{
			e.printStackTrace();
			throw new VCSProviderException( e );
		}

		ProjectInfo info = project.getProjectInfo();
		info.setLastUpdated( new Date() );
		info.setRevision( handler.getRevision() + "" );
		projectSvnInfoDAO.save( info );
	}

	@Override
	public void getLogs( Project project ) throws VCSProviderException
	{
		StringBuilder buff = new StringBuilder();
		LogActionHandler handler = new LogActionHandler( le ->
		{
			String logEntry = String
				.format( "%s | %s | %s<EOL>\n", le.getDate(), le.getAuthor(), le.getMessage() );
			buff.append( logEntry );
		} );

		try
		{
			doSvnAction( project, handler );
		}
		catch ( SVNException | IOException | GeneralSecurityException e )
		{
			e.printStackTrace();
			throw new VCSProviderException( e );
		}

		project.getProjectInfo().setLogEntries( buff.toString() );
		projectSvnInfoDAO.save( project.getProjectInfo() );
	}

	@Override
	public void getInfo( Project project ) throws VCSProviderException
	{
		InfoActionHandler handler = new InfoActionHandler();

		try
		{
			doSvnAction( project, handler );
		}
		catch ( SVNException | IOException | GeneralSecurityException e )
		{
			e.printStackTrace();
			throw new VCSProviderException( e );
		}

		SVNInfo info = handler.getInfo();
		project.getProjectInfo().setProjectUrl( removeUsernameFromUrl( info.getURL()  ) );
		projectSvnInfoDAO.save( project.getProjectInfo() );
	}

	private String removeUsernameFromUrl( SVNURL svnUrl )
	{
		String url = svnUrl.toString();
		String userInfo = svnUrl.getUserInfo();
		if ( StringUtils.isNotBlank( userInfo ) )
		{
			return url.replace( userInfo + "@", "" );
		}
		return url;
	}

	private void doSvnAction( Project project, SvnActionHandler handler ) throws SVNException, IOException, GeneralSecurityException
	{
		VCSCredential creds = project.getCredentials();

		String passClear = new EncryptionUtil().decrypt( creds.getPassword(), secret.toCharArray(), salt.getBytes( "UTF-8" ) );

		SvnActionContext context = new SvnActionContext();
		context.project = project;
		context.svnProjectDir = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		context.manager = SVNClientManager.newInstance( null, creds.getUsername(), passClear );

		try
		{
			handler.handle( context );
		}
		finally
		{
			context.manager.dispose();
		}
	}
}















