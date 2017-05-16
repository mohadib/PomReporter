package org.openactive.PomReporter.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class GitPullHandler implements GitActionHandler
{
	private String revision;

	@Override
	public void handle( GitActionContext context ) throws GitAPIException
	{
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repo = null;

		try
		{
			repo = builder.setGitDir(new File( context.projectDir, ".git")).readEnvironment().findGitDir().build();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		try( Git git = new Git( repo ) )
		{
			PullResult result = git.pull()
				.setStrategy( MergeStrategy.THEIRS )
				.setCredentialsProvider(
					new UsernamePasswordCredentialsProvider( context.username, context.password ) )
				.call();

			revision = result.getMergeResult().getNewHead().name();
		}
	}

	public String getRevision()
	{
		return revision;
	}
}
