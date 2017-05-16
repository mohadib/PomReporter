package org.openactive.PomReporter.svn;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.VCSCredential;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

public interface SvnActionHandler
{
	void handle( SvnActionContext context ) throws SVNException;

	default SVNURL createUrl( Project project ) throws SVNException
	{
		SVNURL baseUrl = SVNURL.parseURIEncoded( project.getUrl() );

		return SVNURL.create(
			baseUrl.getProtocol(),
			project.getCredentials().getUsername(),
			baseUrl.getHost(),
			baseUrl.getPort(),
			baseUrl.getPath(),
			false
		);
	}
}
