package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.ProjectSvnInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mohadib on 1/24/17.
 */
public interface ProjectSvnInfoDAO extends JpaRepository<ProjectSvnInfo, Integer>
{
}
