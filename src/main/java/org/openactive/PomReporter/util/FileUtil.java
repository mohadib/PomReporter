package org.openactive.PomReporter.util;

import org.apache.commons.lang3.StringUtils;
import org.openactive.PomReporter.domain.Project;

import java.io.File;

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
		String projectNameClean = project.getName().replaceAll( String.format("[^%s]", allowedChars), "" );
		String projectPathClean = project.getPath().replaceAll( String.format("[^%s]", allowedChars), "" );

		if( StringUtils.isBlank( projectNameClean ) || StringUtils.isBlank( projectPathClean ))
		{
			throw new IllegalArgumentException( "Name or path blank after cleaning" );
		}

		File basePath = getOrCreateDir( baseFilePath );

		File projectBasePath = new File( basePath, projectNameClean);
		projectBasePath = getOrCreateDir( projectBasePath.getAbsolutePath() );

		File svnProjectDir = new File( projectBasePath, projectPathClean );
		return getOrCreateDir( svnProjectDir.getAbsolutePath() );
	}
}
