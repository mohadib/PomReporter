package org.openactive.PomReporter.util;

import org.apache.commons.lang3.StringUtils;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectSvnInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jdavis on 1/24/17.
 */
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
		ProjectSvnInfo svnInfo = project.getSvnInfo();
		File returnDir = null;
		File baseDir = getOrCreateDir( baseFilePath );

		if( svnInfo != null )
		{
			returnDir = new File( svnInfo.getFilePath() );
		}
		else
		{
			String projectNameClean = project.getName().replaceAll( String.format("[^%s]", allowedChars), "" );
			String projectPathClean = project.getPath().replaceAll( String.format("[^%s]", allowedChars), "" );

			if( StringUtils.isBlank( projectNameClean ) || StringUtils.isBlank( projectPathClean ))
			{
				throw new IllegalArgumentException( "Name or path blank after cleaning" );
			}

			returnDir = new File( new File( baseDir, projectNameClean), projectPathClean );
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
