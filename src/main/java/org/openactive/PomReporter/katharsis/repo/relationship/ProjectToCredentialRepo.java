package org.openactive.PomReporter.katharsis.repo.relationship;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.stereotype.Repository;

/**
 * Created by jdavis on 1/24/17.
 */
@Repository
public class ProjectToCredentialRepo extends RelationshipRepositoryBase<Project, Integer, SvnCredential, Integer>
{
	public ProjectToCredentialRepo()
	{
		super( Project.class, SvnCredential.class );
	}
}
