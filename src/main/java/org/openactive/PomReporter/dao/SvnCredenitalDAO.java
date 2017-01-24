package org.openactive.PomReporter.dao;

import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mohadib on 1/23/17.
 */
public interface SvnCredenitalDAO extends JpaRepository<SvnCredential, Integer >
{
  SvnCredential findByName(String name );
}
