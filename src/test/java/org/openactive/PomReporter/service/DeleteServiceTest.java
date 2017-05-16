package org.openactive.PomReporter.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.ProjectInfoDAO;
import org.openactive.PomReporter.dao.VCSCredenitalDAO;
import org.openactive.PomReporter.domain.*;
import org.openactive.PomReporter.domain.ProjectInfo;
import org.openactive.PomReporter.exceptions.LockTimeoutException;
import org.openactive.PomReporter.service.impl.LockServiceImpl;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "/applicationContext.xml" } )
public class DeleteServiceTest
{
	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private VCSCredenitalDAO credenitalDAO;

	@Autowired
	private ProjectGroupDAO projectGroupDAO;

	@Autowired
	private DeleteService deleteService;

	@Autowired
	private ProjectInfoDAO projectSvnInfoDAO;

	@Autowired
	private LockServiceImpl lockService;

	@Value( "${svn.checkoutProject.base.path}" )
	private String svnBaseDir;

	@Value( "${svn.checkoutProject.base.path.fileName.allowed.chars}" )
	private String allowedChars;

	@Autowired
	private DataSource dataSource;

	private Project project;
	private VCSCredential cred;
	private ProjectGroup group;
	private File projectDir;
	private File pom;
	private ProjectInfo info;

	@Before
	public void setup() throws Exception
	{
		cred = new VCSCredential();
		cred.setName( "test1" );
		cred.setUsername( "JohnQUser" );
		cred.setPassword( "letmein" );
		cred = credenitalDAO.save( cred );

		group = new ProjectGroup();
		group.setName( "Project Group" );
		group.setDefault( true );
		group = projectGroupDAO.save( group );

		project = new Project();
		project.setCredentials( cred );
		project.setName( "Test Project" );
		project.setUrl( "http://svn.company.com/svn/some/project" );
		project.setXpathExpression( "/project/version/text()" );
		project.setProjectGroup( group );
		project = projectDAO.save( project );

		FileUtil util = new FileUtil();
		projectDir = util.getOrCreateSvnProjectDir( project, svnBaseDir, allowedChars );
		pom = new File( projectDir, "pom.xml" );
		pom.createNewFile();

		info = new ProjectInfo();
		info.setProjectUrl( "http://foo.com/svn/some/project" );
		info.setRevision( "33" );
		info.setFilePath( projectDir.getAbsolutePath() );
		info.setCreated( new Date() );
		info.setLastUpdated( new Date() );
		info.setLogEntries( "Logs be here" );
		info.setXpathResult( "result text" );
		info.setProject( project );
		info = projectSvnInfoDAO.save( info );

		//refresh project so it has svnInfo link
		project = projectDAO.findOne( project.getId() );

		//refresh projectgroup to have link to projects
		group = projectGroupDAO.findOne( project.getId() );
	}

	@After
	public void cleanup() throws Exception
	{
		Connection con = dataSource.getConnection();
		Statement stmnt = con.createStatement();
		stmnt.execute( "DELETE FROM ProjectSvnInfo" );
		stmnt.execute( "DELETE FROM project" );
		stmnt.execute( "DELETE FROM projectgroup" );
		stmnt.execute( "DELETE FROM SvnCredential" );
		stmnt.close();
		con.close();

		try
		{
			FileUtils.deleteDirectory( new File( svnBaseDir ) );
		}
		catch ( Exception e )
		{
		}
	}

	@Test
	public void testDeleteProject() throws Exception
	{
		deleteService.deleteProject( project );
		assertNull( projectDAO.findOne( project.getId() ) );
		assertFalse( projectDir.exists() );

		//svnInfo should be gone..
		assertNull( projectSvnInfoDAO.findOne( info.getId() ) );

		//cred and project group should still exist
		assertNotNull( credenitalDAO.findOne( cred.getId() ) );
		assertNotNull( projectGroupDAO.findOne( group.getId() ) );
	}

	@Test
	public void testDeleteCredential() throws Exception
	{
		deleteService.deleteCredentials( cred );

		//cred should be gone
		assertNull( credenitalDAO.findOne( cred.getId() ) );

		//any projects with cred should be deleted
		assertNull( projectDAO.findOne( project.getId() ) );
		assertFalse( projectDir.exists() );

		//svn info should be gone
		assertNull( projectSvnInfoDAO.findOne( info.getId() ) );

		//group should still exist
		assertNotNull( projectGroupDAO.findOne( group.getId() ) );

		//group should not have any projects
		assertEquals( 0, projectGroupDAO.findOne( group.getId() ).getProjects().size() );
	}

	@Test
	public void testDeleteProjectGroup() throws Exception
	{
		deleteService.deleteProjectGroup( group );
		assertNull( projectGroupDAO.findOne( group.getId() ) );
		assertNotNull( projectDAO.findOne( project.getId() ) );
		assertNotNull( credenitalDAO.findOne( cred.getId() ) );
	}

	// test that an exception will be thrown if lock can not be acquired.
	@Test
	public void testLockTimeout() throws Exception
	{
		Lock lock = lockService.getPomServiceRunLock();
		AtomicBoolean threwException = new AtomicBoolean( false );
		if ( lock.tryLock() )
		{
			try
			{
				Thread t = new Thread( () ->
				{
					try
					{
						deleteService.deleteProject( project );
					}
					catch ( Exception e )
					{
						threwException.set( e instanceof LockTimeoutException );
					}
				} );
				t.start();
				t.join();
				assertTrue( threwException.get() );
			}
			finally
			{
				lock.unlock();
			}
		}
		else
		{
			fail( "Could not acquire lock" );
		}
	}

	// test that if cant lock at first, but then can, no exception... :S
	@Test
	public void testLockTimeoutSuccess() throws Exception
	{
		Lock lock = lockService.getPomServiceRunLock();
		AtomicBoolean threwException = new AtomicBoolean( false );

		Thread t = new Thread( () ->
		{
			try
			{
				deleteService.deleteProject( project );
			}
			catch ( Exception e )
			{
				threwException.set( e instanceof LockTimeoutException );
			}
		} );

		if ( lock.tryLock() )
		{
			try
			{
				t.start();
			}
			finally
			{
				lock.unlock();
			}
			t.join();
			assertFalse( threwException.get() );
			assertNull( projectDAO.findOne( project.getId() ) );
			assertFalse( projectDir.exists() );
		}
		else
		{
			fail( "Could not acquire lock" );
		}
	}
}
