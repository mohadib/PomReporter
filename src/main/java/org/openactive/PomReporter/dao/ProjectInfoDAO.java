package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInfoDAO extends JpaRepository<ProjectInfo, Integer>
{
}
