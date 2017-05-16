package org.openactive.PomReporter.git;

import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Created by jdavis on 5/16/17.
 */
public interface GitActionHandler
{
	void handle( GitActionContext context ) throws GitAPIException;
}
