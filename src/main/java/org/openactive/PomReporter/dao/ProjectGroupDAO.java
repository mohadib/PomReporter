package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jdavis on 1/24/17.
 */
public interface ProjectGroupDAO extends JpaRepository<ProjectGroup, Integer >
{
	ProjectGroup findByName(String name );
}
