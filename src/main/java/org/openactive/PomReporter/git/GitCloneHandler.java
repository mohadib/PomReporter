package org.openactive.PomReporter.git;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;


public class GitCloneHandler implements  GitActionHandler
{
	private File projectDir;

	@Override
	public void handle( GitActionContext context ) throws GitAPIException
	{
		projectDir = context.projectDir;

		CloneCommand command = Git.cloneRepository()
			.setURI( context.project.getUrl() )
			.setCredentialsProvider( new UsernamePasswordCredentialsProvider( context.username, context.password ) )
			.setDirectory( context.projectDir );

		if( StringUtils.isNotBlank( context.project.getBranch() ) )
		{
			command.setBranch( context.project.getBranch() );
		}

		try( Git result = command.call() )
		{
			System.out.println(result);
		}
	}

	public File getProjectDir()
	{
		return projectDir;
	}
}
