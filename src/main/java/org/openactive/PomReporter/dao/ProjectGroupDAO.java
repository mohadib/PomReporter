package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jdavis on 1/24/17.
 */
public interface ProjectGroupDAO extends JpaRepository<ProjectGroup, Integer >
{
	ProjectGroup findByName(String name );

	@Transactional
	@Modifying
	@Query("update ProjectGroup pg set pg.isDefault = false")
	void removeDefault();
}
