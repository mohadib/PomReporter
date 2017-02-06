package org.openactive.PomReporter.exceptions;

import java.util.concurrent.TimeoutException;

/**
 * Created by mohadib on 2/5/17.
 */
public class LockTimeoutException extends TimeoutException
{
  public LockTimeoutException(String message)
  {
    super(message);
  }
}
