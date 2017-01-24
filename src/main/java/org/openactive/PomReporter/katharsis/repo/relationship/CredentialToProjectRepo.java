package org.openactive.PomReporter.katharsis.repo.relationship;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.stereotype.Repository;

/**
 * Created by jdavis on 1/24/17.
 */
@Repository
public class CredentialToProjectRepo extends RelationshipRepositoryBase<SvnCredential, Integer, Project, Integer>
{
	public CredentialToProjectRepo()
	{
		super( SvnCredential.class, Project.class );
	}
}
