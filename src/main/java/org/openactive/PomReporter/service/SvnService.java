package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.tmatesoft.svn.core.SVNException;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by mohadib on 1/24/17.
 */
public interface SvnService
{
  void checkoutProject(Project project) throws SVNException, IOException, GeneralSecurityException;

  void updateProject(Project project) throws SVNException, IOException, GeneralSecurityException;

  void getLogs(Project project) throws SVNException, IOException, GeneralSecurityException;

  void getInfo(Project project) throws SVNException, IOException, GeneralSecurityException;
}
