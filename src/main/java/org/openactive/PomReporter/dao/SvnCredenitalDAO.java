package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by mohadib on 1/23/17.
 */
public interface SvnCredenitalDAO extends JpaRepository<SvnCredential, Integer >
{
  SvnCredential findByName(String name );

  @Query("SELECT x FROM SvnCredential x JOIN FETCH x.projects WHERE x.name = (:name)")
  SvnCredential findByNameAndFetchProjectsEagerly(@Param("name") String name );

}
