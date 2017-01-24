package org.openactive.PomReporter.util;

import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by mohadib on 1/23/17.
 */
@Component
public class LoadData
{
  @Autowired
  private SvnCredenitalDAO svnCredRepo;

  @EventListener({ContextRefreshedEvent.class})
  public void appStarted()
  {
    svnCredRepo.save( new SvnCredential( "jason", "letmein", "Default"));
    svnCredRepo.save( new SvnCredential( "bob", "money", "bobrepo"));
  }
}
