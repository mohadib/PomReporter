package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;

/**
 * Created by jdavis on 1/25/17.
 */
public interface DeleteService
{
	void deleteProject( Project project ) throws Exception;

	void deleteCredentials( SvnCredential credential ) throws Exception;
}
