package org.openactive.PomReporter.svn;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import java.io.File;

/**
 * Created by mohadib on 1/24/17.
 */
public class CreateActionHandler implements SvnActionHandler
{
  private File svnProjectDir;

  @Override
  public void handle(SvnActionContext context) throws SVNException
  {
    svnProjectDir = context.svnProjectDir;
    SVNUpdateClient client = context.manager.getUpdateClient();
    client.doCheckout( createUrl( context.project ), context.svnProjectDir, null, SVNRevision.HEAD, SVNDepth.EMPTY, false);
  }

  public File getSvnProjectDir()
  {
    return svnProjectDir;
  }
}
