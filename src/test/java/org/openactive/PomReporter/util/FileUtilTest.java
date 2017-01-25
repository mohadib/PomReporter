package org.openactive.PomReporter.util;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectSvnInfo;

import java.io.File;
import java.io.IOException;

/**
 * Created by jdavis on 1/24/17.
 */
public class FileUtilTest
{
	private final String baseFilePath = "/tmp/projects";

	@After
	public void cleanup() throws IOException
	{
		File base = new File( baseFilePath );
		if( base.exists() && base.getAbsolutePath().startsWith( "/tmp" ))
		{
			FileUtils.deleteDirectory( new File( baseFilePath ) );
		}
	}

	@Test
	public void testGetOrCreateSvnProjectDirHappy()
	{
		Project project = new Project();
		project.setName( "Foo" );
		project.setPath( "svn/baz/foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/Foo/svnbazfoo", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateSvnProjectDirBadChars()
	{
		Project project = new Project();
		project.setName( "../Foo" );
		project.setPath( "svn/baz/../../foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/Foo/svnbazfoo", created.getAbsolutePath() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testGetOrCreateSvnProjectDirAllProjectNameBadChars()
	{
		Project project = new Project();
		project.setName( "#$%%^&$%&$%&" );
		project.setPath( "svn/baz/../../foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/Foo/svnbazfoo", created.getAbsolutePath() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testGetOrCreateSvnProjectDirAllProjectPathBadChars()
	{
		Project project = new Project();
		project.setName( "Foo" );
		project.setPath( "../../../" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/Foo/svnbazfoo", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateSvnProjectDirWithPathOnProject()
	{
		String allowedChars = "a-zA-Z0-9_@";
		Project project = new Project();
		ProjectSvnInfo svnInfo = new ProjectSvnInfo();
		svnInfo.setFilePath( "/tmp/projects/bar");
		project.setSvnInfo( svnInfo );
		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars);
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testGetOrCreateSvnProjectDirWithBadPathOnProject()
	{
		String allowedChars = "a-zA-Z0-9_@";
		Project project = new Project();
		ProjectSvnInfo svnInfo = new ProjectSvnInfo();
		svnInfo.setFilePath( "/projects/bar");
		project.setSvnInfo( svnInfo );
		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars);
	}
}
