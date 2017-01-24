package org.openactive.PomReporter.katharsis.repo;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.annotations.*;
import io.katharsis.resource.list.ResourceList;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by mohadib on 1/23/17.
 */
@Repository
@JsonApiResourceRepository(SvnCredential.class)
public class SvnCredentialsRepo
{
  @Autowired
  private SvnCredenitalDAO dao;

  @JsonApiFindOne
  public SvnCredential findOne(Integer id, QuerySpec querySpec)
  {
    return null;
  }

  @JsonApiFindAll
  public ResourceList<SvnCredential> findAll(QuerySpec querySpec)
  {
    return querySpec.apply( dao.findAll() );
  }

  @JsonApiFindAllWithIds
  public ResourceList<SvnCredential> findAll(Iterable<Integer> ids, QuerySpec querySpec)
  {
    return null;
  }

  @JsonApiSave
  public SvnCredential save(SvnCredential resource)
  {
    return null;
  }

  @JsonApiDelete
  public void delete(Integer id)
  {
  }
}
