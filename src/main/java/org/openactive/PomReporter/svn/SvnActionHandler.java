package org.openactive.PomReporter.svn;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

/**
 * Created by mohadib on 1/24/17.
 */
public interface SvnActionHandler
{
  void handle( SvnActionContext context ) throws SVNException;

  default SVNURL createUrl(Project project ) throws SVNException
  {
    SvnCredential creds = project.getCredentials();
    return SVNURL.create(
      creds.getProtocol(),
      creds.getUsername(),
      creds.getHost(),
      creds.getPort(),
      project.getPath(),
      false
    );
  }
}
