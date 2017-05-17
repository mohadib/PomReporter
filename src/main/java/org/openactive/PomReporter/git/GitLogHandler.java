package org.openactive.PomReporter.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitLogHandler implements GitActionHandler
{
	private String commentBlob;

	@Override
	public void handle( GitActionContext context ) throws GitAPIException
	{
		try
		{
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repo = builder.setGitDir( new File( context.projectDir, ".git" ) )
				.readEnvironment().findGitDir().build();

			StringBuilder buff = new StringBuilder();
			try( Git git = new Git( repo ) )
			{
				Iterable< RevCommit > logs = git.log().setMaxCount( 20 ).call();
				for ( RevCommit commit : logs )
				{
					GitRevParser parser = new GitRevParser();
					parser.parse( commit, context.project );
					String logEntry = String.format(
						"%s | %s | %s | %s<EOL>",
						parser.getCommitDate(),
						parser.getGitlabCommitUrl() ,
						commit.getAuthorIdent().getName(),
						parser.getDisplayedMessage()
					);

					buff.append( logEntry );
				}
			}
			commentBlob = buff.toString();
		}
		catch ( IOException ioe  )
		{
			throw new TransportException( ioe.getMessage(), ioe );
		}
	}

	public String getCommentBlob()
	{
		return commentBlob;
	}
}
