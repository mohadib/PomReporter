package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;

/**
 * Created by mohadib on 1/24/17.
 */
public interface PomService
{
  void parsePom(Project project) throws Exception;
}
