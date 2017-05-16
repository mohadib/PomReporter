package org.openactive.PomReporter.git;

import org.openactive.PomReporter.domain.Project;

import java.io.File;

public class GitActionContext
{
	public File projectDir;
	public Project project;
	public String username;
	public String password;
}
