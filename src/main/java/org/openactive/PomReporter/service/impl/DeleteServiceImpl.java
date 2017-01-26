package org.openactive.PomReporter.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by jdavis on 1/25/17.
 */
@Service
public class DeleteServiceImpl implements org.openactive.PomReporter.service.DeleteService
{
	private final static Logger LOG = Logger.getLogger( DeleteServiceImpl.class );

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private SvnCredenitalDAO svnCredenitalDAO;

	@Autowired
	private LockServiceImpl lockService;

	@Value("${svn.checkoutProject.base.path}")
	private String baseFilePath;

	@Override public void deleteProject( Project project ) throws Exception
	{
		Lock pomRunLock = lockService.getPomServiceRunLock();
		if( pomRunLock.tryLock( 90, TimeUnit.SECONDS ) )
		{
			try
			{
				deleteFiles( project );

				//delete project and child entities
				projectDAO.delete( project );
			}
			finally
			{
				pomRunLock.unlock();
			}
		}
	}

	@Override
	public void deleteCredentials( SvnCredential credential ) throws Exception
	{
		Lock pomRunLock = lockService.getPomServiceRunLock();
		if( pomRunLock.tryLock( 90, TimeUnit.SECONDS ) )
		{
			try
			{
				List<Project> projects = projectDAO.findAllByCredentials( credential );
				for( Project project : projects )
				{
					deleteFiles( project );
				}

				svnCredenitalDAO.delete( credential );

			}
			finally
			{
				pomRunLock.unlock();
			}
		}
	}

	private void deleteFiles( Project project ) throws Exception
	{
		//delete project from file system
		File svnProjectDir = new File( project.getSvnInfo().getFilePath() );
		File parent = svnProjectDir.getParentFile();
		File base = new File( baseFilePath );

		if( parent.getParentFile().equals( base ))
		{
			FileUtils.deleteDirectory( parent );
		}
		else
		{
			LOG.error( "TRYING TO DELETE FILE NOT IN BASE " + parent.getAbsolutePath() );
			throw new IllegalArgumentException( parent.getAbsolutePath() );
		}
	}
}
