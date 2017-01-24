package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.openactive.PomReporter.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import java.io.File;

/**
 * Created by jdavis on 1/24/17.
 */
public class SvnServiceImpl
{
	@Value( "${svn.checkout.base.path.fileName.allowed.chars}" )
	private String allowedChars;

	@Value( "${svn.checkout.base.path}" )
	private String baseFilePath;

	public void checkout( Project project ) throws SVNException
	{
		File svnProjectDir = new FileUtil().getOrCreateSvnProjectDir( project, baseFilePath, allowedChars );

		SvnCredential creds = project.getCredentials();
		SVNClientManager svn = SVNClientManager.newInstance( null, creds.getUsername(), creds.getPassword());
		SVNUpdateClient client = svn.getUpdateClient();


		SVNURL url = SVNURL.create(
						creds.getProtocol(),
						creds.getUsername(),
						creds.getHost(),
						creds.getPort(),
						project.getPath(),
						false
					);

		// checkout project with empty depth so we dont get the whole structure
		long rev = client.doCheckout( url, svnProjectDir, null, SVNRevision.HEAD, SVNDepth.EMPTY, false );

		// update just the pom so thats the only file we get.
		File pom = new File( svnProjectDir, "pom.xml");
		long prev = client.doUpdate( pom , SVNRevision.HEAD, SVNDepth.EMPTY, false, false );



		svn.dispose();
	}


}
