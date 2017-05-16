package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.VCSCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VCSCredenitalDAO extends JpaRepository<VCSCredential, Integer >
{
  VCSCredential findByName(String name );

  @Query("SELECT x FROM VCSCredential x LEFT JOIN FETCH x.projects WHERE x.name = (:name)")
  VCSCredential findByNameAndFetchProjectsEagerly(@Param("name") String name );

  @Query("select x from VCSCredential x left join fetch x.projects where x.id = (:id)")
  VCSCredential findByIdAndFetchProjectsEagerly( @Param( "id" ) Integer id);

  @Query("SELECT x FROM VCSCredential x LEFT JOIN FETCH x.projects")
  List<VCSCredential > findAllAndFetchProjectsEagerly();
}
