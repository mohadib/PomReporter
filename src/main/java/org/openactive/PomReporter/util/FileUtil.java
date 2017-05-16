package org.openactive.PomReporter.util;

import org.apache.commons.lang3.StringUtils;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil
{
	private File getOrCreateDir( String path )
	{
		File basePath = new File( path );
		if( !basePath.exists() && !basePath.mkdirs())
		{
			throw new IllegalArgumentException( "Could not create dir :" + path );
		}
		else if( basePath.isFile() )
		{
			throw new IllegalArgumentException( "path exists and is not a directory :" + path );
		}
		else if( !basePath.canWrite() )
		{
			throw new IllegalArgumentException( "Cant write to path :" + path  );
		}

		return  basePath;
	}

	public File getOrCreateSvnProjectDir( Project project, String baseFilePath, String allowedChars )
	{
		ProjectInfo svnInfo = project.getProjectInfo();
		File returnDir;
		File baseDir = getOrCreateDir( baseFilePath );

		if( svnInfo != null )
		{
			returnDir = new File( svnInfo.getFilePath() );
		}
		else
		{
			// http://svn.foo.com/svn/project/branches/mybranch
			// https://git.foo.com/svn/project/project.git

			String projectNameClean = project.getName().replaceAll( String.format("[^%s]+", allowedChars), "_" );
			String projectPathClean = project.getUrl().replaceAll( String.format("[^%s]+", allowedChars), "_" );

			if( StringUtils.isBlank( projectNameClean ) || StringUtils.isBlank( projectPathClean ))
			{
				throw new IllegalArgumentException( "Name or path blank after cleaning" );
			}

			returnDir = new File( new File( baseDir, projectPathClean ), projectNameClean );
		}

		Path child = Paths.get( returnDir.getAbsolutePath() ).toAbsolutePath();
		Path parent = Paths.get( baseDir.getAbsolutePath()).toAbsolutePath();
		if( !child.startsWith(parent) )
		{
			throw new IllegalArgumentException( "Child is not in parent :" + returnDir.getAbsolutePath() );
		}

		return getOrCreateDir( returnDir.getAbsolutePath() );
	}
}
