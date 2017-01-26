package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jdavis on 1/24/17.
 */
public interface ProjectDAO extends JpaRepository<Project, Integer>
{
	Project findByName(String name );

	@Query("SELECT x from Project x JOIN FETCH x.projectGroup where x.name = (:name)")
	Project findByNameWithProjectEagerlyLoaded(@Param("name") String name );

	@Query("select x from Project x join fetch x.projectGroup")
	List<Project> findAllWithProjectsEagerlyLoaded();

	List<Project> findAllByCredentials( SvnCredential credential );
}
