package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.openactive.PomReporter.domain.VCSCredential;

/**
 * Created by jdavis on 1/25/17.
 */
public interface DeleteService
{
  void deleteProject(Project project) throws Exception;

  void deleteCredentials(VCSCredential credential) throws Exception;

  void deleteProjectGroup(ProjectGroup projectGroup) throws Exception;
}
