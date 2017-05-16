package org.openactive.PomReporter.util;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectInfo;

import java.io.File;
import java.io.IOException;

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
		project.setUrl( "http://svn.foo.org/svn/baz/foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/http_svn_foo_org_svn_baz_foo/Foo", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateGitProjectDirHappy()
	{
		Project project = new Project();
		project.setName( "Foo" );
		project.setUrl( "http://git.foo.org/baz/foo.git" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/http_git_foo_org_baz_foo_git/Foo", created.getAbsolutePath() );
	}

	@Test
	public void testTwoProjectsSameUrlHappy()
	{
		String allowedChars = "a-zA-Z0-9_@";

		Project project = new Project();
		project.setName( "Foo" );
		project.setUrl( "http://git.foo.org/baz/foo.git" );

		Project project1 = new Project();
		project1.setName( "Bar" );
		project1.setUrl( "http://git.foo.org/baz/foo.git" );

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		File created1 = new FileUtil().getOrCreateSvnProjectDir( project1, baseFilePath, allowedChars );

		assertNotNull( created );
		assertNotNull( created1 );
		assertEquals( "/tmp/projects/http_git_foo_org_baz_foo_git/Foo", created.getAbsolutePath() );
		assertEquals( "/tmp/projects/http_git_foo_org_baz_foo_git/Bar", created1.getAbsolutePath() );
		assertEquals( created.getParent(), created1.getParent() );
	}


	@Test
	public void testGetOrCreateSvnProjectDirBadChars()
	{
		Project project = new Project();
		project.setName( "../Foo" );
		project.setUrl( "http://svn.foo.org/svn/baz/../../foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/http_svn_foo_org_svn_baz_foo/_Foo", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateSvnProjectDirAllProjectNameBadChars()
	{
		Project project = new Project();
		project.setName( "#$%%^&$%&$%&" );
		project.setUrl( "http://svn.foo.org/svn/baz/../../foo" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/http_svn_foo_org_svn_baz_foo/_", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateSvnProjectDirAllProjectPathBadChars()
	{
		Project project = new Project();
		project.setName( "Foo" );
		project.setUrl( "../../../" );

		String allowedChars = "a-zA-Z0-9_@";

		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );
		assertNotNull( created );
		assertTrue( created.exists() );
		assertTrue( created.canWrite() );
		assertEquals( "/tmp/projects/_/Foo", created.getAbsolutePath() );
	}

	@Test
	public void testGetOrCreateSvnProjectDirWithPathOnProject()
	{
		String allowedChars = "a-zA-Z0-9_@";
		Project project = new Project();
		ProjectInfo svnInfo = new ProjectInfo();
		svnInfo.setFilePath( "/tmp/projects/bar");
		project.setProjectInfo( svnInfo );
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
		ProjectInfo svnInfo = new ProjectInfo();
		svnInfo.setFilePath( "/projects/bar");
		project.setProjectInfo( svnInfo );
		File created = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars);
	}
}
