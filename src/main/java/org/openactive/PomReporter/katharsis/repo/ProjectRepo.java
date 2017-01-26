package org.openactive.PomReporter.katharsis.repo;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindAllWithIds;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;
import io.katharsis.resource.list.ResourceList;
import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jdavis on 1/24/17.
 */
@Repository
@JsonApiResourceRepository( Project.class )
public class ProjectRepo
{
	@Autowired
	private ProjectDAO dao;

	@JsonApiFindOne
	public Project findOne(Integer id, QuerySpec querySpec)
	{
		return dao.findOne( id );
	}

	@JsonApiFindAll
	public ResourceList<Project> findAll( QuerySpec querySpec )
	{
		return querySpec.apply( dao.findAllWithProjectsEagerlyLoaded() );
	}

	@JsonApiFindAllWithIds
	public ResourceList<Project> findAll(Iterable<Integer> ids, QuerySpec querySpec)
	{
		return querySpec.apply( dao.findAll( ids ) );
	}
}
