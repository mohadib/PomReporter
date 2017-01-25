package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.tmatesoft.svn.core.SVNException;

/**
 * Created by mohadib on 1/24/17.
 */
public interface SvnService
{
  void checkoutProject(Project project) throws SVNException;

  void updateProject(Project project) throws SVNException;

  void getLogs(Project project) throws SVNException;

  void getInfo(Project project) throws SVNException;
}
