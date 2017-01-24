package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jdavis on 1/24/17.
 */
public interface ProjectDAO extends JpaRepository<Project, Integer>
{
	Project findByName(String name );
}
