package org.openactive.PomReporter.service.impl;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.openactive.PomReporter.dao.ProjectInfoDAO;
import org.openactive.PomReporter.domain.*;
import org.openactive.PomReporter.exceptions.VCSProviderException;
import org.openactive.PomReporter.git.*;
import org.openactive.PomReporter.service.VCSProvider;
import org.openactive.PomReporter.util.EncryptionUtil;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@Service
@Qualifier("gitVcsService")
public class GitVCSService implements VCSProvider
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
		GitCloneHandler handler = new GitCloneHandler();
		doGitAction( project, handler );

		ProjectInfo info = new ProjectInfo();
		info.setProject( project );
		info.setCreated( new Date() );
		info.setFilePath( handler.getProjectDir().getAbsolutePath() );
		info.setProjectUrl( project.getUrl().replaceFirst("\\.git$", "") );
		projectSvnInfoDAO.save( info );
	}

	@Override
	public void update( Project project ) throws VCSProviderException
	{
		GitPullHandler handler = new GitPullHandler();
		doGitAction( project, handler );

		ProjectInfo info = project.getProjectInfo();
		info.setLastUpdated( new Date() );
		info.setRevision( handler.getRevision() );
		projectSvnInfoDAO.save( info );
	}

	@Override
	public void getLogs( Project project ) throws VCSProviderException
	{
		GitLogHandler handler = new GitLogHandler();
		doGitAction( project, handler );
		project.getProjectInfo().setLogEntries( handler.getCommentBlob() );
		projectSvnInfoDAO.save( project.getProjectInfo() );
	}

	@Override
	public void getInfo( Project project ) throws VCSProviderException
	{
		// noop for git repo
	}

	private void doGitAction( Project project, GitActionHandler handler ) throws VCSProviderException
	{
		try
		{
			VCSCredential creds = project.getCredentials();
			String passClear = new EncryptionUtil().decrypt( creds.getPassword(), secret.toCharArray(), salt.getBytes( "UTF-8" ) );

			GitActionContext context = new GitActionContext();
			context.project = project;
			context.password = passClear;
			context.username = creds.getUsername();
			context.projectDir = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );

			handler.handle( context );
		}
		catch ( IOException | GitAPIException | GeneralSecurityException e )
		{
			throw new VCSProviderException( e );
		}
	}
}























