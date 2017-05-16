package org.openactive.PomReporter.service;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.exceptions.VCSProviderException;

public interface VCSProvider
{
	void checkout( Project project ) throws VCSProviderException;
	void update( Project project ) throws VCSProviderException;
	void getLogs( Project project ) throws VCSProviderException;
	void getInfo( Project project ) throws VCSProviderException;
}
