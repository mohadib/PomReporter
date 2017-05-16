package org.openactive.PomReporter.svn;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import java.io.File;

public class UpdateActionHandler implements SvnActionHandler
{
  private long revision;

  @Override
  public void handle(SvnActionContext context) throws SVNException
  {
    SVNUpdateClient client = context.manager.getUpdateClient();
    revision = client.doUpdate( new File( context.svnProjectDir, "pom.xml"), SVNRevision.HEAD, SVNDepth.FILES, false, false);
  }

  public long getRevision()
  {
    return revision;
  }
}
