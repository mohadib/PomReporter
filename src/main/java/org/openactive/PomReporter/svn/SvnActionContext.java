package org.openactive.PomReporter.svn;

import org.openactive.PomReporter.domain.Project;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import java.io.File;

/**
 * Created by mohadib on 1/24/17.
 */
public class SvnActionContext
{
  public SVNClientManager manager;
  public Project project;
  public File svnProjectDir;
}
