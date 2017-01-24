package org.openactive.PomReporter.katharsis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by mohadib on 1/23/17.
 */
@Component
public class SpringApplicationContextListener implements ServletContextAware, ApplicationContextAware
{
  private ApplicationContext ctx;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
  {
    ctx = applicationContext;
  }

  @Override
  public void setServletContext(ServletContext servletContext)
  {
    servletContext.setAttribute("applicationContext", ctx);
  }
}
