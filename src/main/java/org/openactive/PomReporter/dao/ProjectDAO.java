package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jdavis on 1/24/17.
 */
public interface ProjectDAO extends JpaRepository<Project, Integer>
{
	Project findByName(String name );
	List<Project> findAllByCredentials( SvnCredential credential );
}
