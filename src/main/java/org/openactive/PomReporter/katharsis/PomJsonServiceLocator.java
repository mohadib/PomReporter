package org.openactive.PomReporter.katharsis;

import io.katharsis.locator.JsonServiceLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by mohadib on 1/23/17.
 */
@Component("jsonServiceLocator")
public class PomJsonServiceLocator implements JsonServiceLocator, ApplicationContextAware
{
  private ApplicationContext ctx;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
  {
    ctx = applicationContext;
  }

  @Override
  public <T> T getInstance(Class<T> aClass)
  {
    return ctx.getBean( aClass );
  }
}
