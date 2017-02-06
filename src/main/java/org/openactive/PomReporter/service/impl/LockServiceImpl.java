package org.openactive.PomReporter.service.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jdavis on 1/25/17.
 *
 *  An application wide lock to keep
 *  deleting of projects, creds, projectgroups from
 *  happening while ProjectUpdateService is running.
 *
 */
@Service
public class LockServiceImpl
{
  private final ReentrantLock pomSerivceRunLock = new ReentrantLock(true);

  public Lock getPomServiceRunLock()
  {
    return pomSerivceRunLock;
  }
}
