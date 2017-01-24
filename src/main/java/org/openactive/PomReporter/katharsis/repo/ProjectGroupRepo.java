package org.openactive.PomReporter.katharsis.repo;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindAllWithIds;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;
import io.katharsis.resource.list.ResourceList;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jdavis on 1/24/17.
 */
@Repository
@JsonApiResourceRepository( ProjectGroup.class )
public class ProjectGroupRepo
{
	@Autowired
	private ProjectGroupDAO dao;

	@JsonApiFindOne
	public ProjectGroup findOne(Integer id, QuerySpec querySpec)
	{
		return dao.findOne( id );
	}

	@JsonApiFindAll
	public ResourceList<ProjectGroup> findAll( QuerySpec querySpec )
	{
		ResourceList<ProjectGroup> ret =  querySpec.apply( dao.findAll() );
		return ret;
	}

	@JsonApiFindAllWithIds
	public ResourceList<ProjectGroup> findAll(Iterable<Integer> ids, QuerySpec querySpec)
	{
		return querySpec.apply( dao.findAll( ids ) );
	}
}
