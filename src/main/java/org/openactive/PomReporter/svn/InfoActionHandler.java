package org.openactive.PomReporter.svn;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Created by mohadib on 1/24/17.
 */
public class InfoActionHandler implements SvnActionHandler
{
  private SVNInfo info;

  @Override
  public void handle(SvnActionContext context) throws SVNException
  {
    SVNWCClient wcClient = context.manager.getWCClient();
    info = wcClient.doInfo( context.svnProjectDir, SVNRevision.HEAD );
  }

  public SVNInfo getInfo()
  {
    return info;
  }
}
