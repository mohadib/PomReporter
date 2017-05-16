package org.openactive.PomReporter.service.impl;

import org.apache.log4j.Logger;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.service.PomService;
import org.openactive.PomReporter.service.VCSProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class ProjectUpdateServiceImpl
{
	private final static Logger LOG = Logger.getLogger( ProjectUpdateServiceImpl.class );

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private PomService pomService;

	@Autowired
	@Qualifier("svnVcsService")
	private VCSProvider svnService;

	@Autowired
	@Qualifier("gitVcsService")
	private VCSProvider gitService;

	@Autowired
	private LockServiceImpl lockService;

	// if svn.update.delay is not setup in properties, then assume we are in dev and basically
	// never run
	@Scheduled( fixedRateString = "${svn.update.rate}", initialDelayString = "${svn.update"
        + ".delay:9223372036854775807}" )
	public void run() throws InterruptedException
	{
		LOG.info( "Running ProjectUpdateService" );
		Lock pomRunLock = lockService.getPomServiceRunLock();
		if ( pomRunLock.tryLock( 60, TimeUnit.SECONDS ) )
		{
			try
			{
				updateProjects();
			}
			finally
			{
				pomRunLock.unlock();
			}
		}
		else
		{
			LOG.info( "Could not acquire lock" );
		}
	}

	private void updateProjects()
	{
		List< Project > projects = projectDAO.findAll();
		for ( Project project : projects )
		{
			VCSProvider vcsProvider = project.getUrl().toLowerCase().endsWith( ".git" ) ?
				gitService : svnService;

			if ( project.getProjectInfo() == null )
			{
				try
				{
					vcsProvider.checkout( project );
					// after checkout ProjectInfo gets created
					// before that its null, lets refresh the project and
					// get the new ProjectInfo
					project = projectDAO.findOne( project.getId() );
				}
				catch ( Exception e )
				{
					//could not checkout!
					LOG.error( e.getMessage(), e );
					continue;
				}
			}

			try
			{
				vcsProvider.update( project );
				vcsProvider.getInfo( project );
				vcsProvider.getLogs( project );
			}
			catch ( Exception e )
			{
				// could not update project, info, or logs
				LOG.error( e.getMessage(), e );
				continue;
			}

			try
			{
				pomService.parsePom( project );
			}
			catch ( Exception e )
			{
				LOG.error( e.getMessage(), e );
			}
		}
	}
}
