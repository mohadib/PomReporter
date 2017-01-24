package org.openactive.PomReporter.katharsis;

import io.katharsis.invoker.KatharsisInvokerBuilder;
import io.katharsis.locator.JsonServiceLocator;
import io.katharsis.servlet.AbstractKatharsisServlet;
import io.katharsis.servlet.KatharsisProperties;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Created by mohadib on 1/23/17.
 */
public class KatharsisServlet extends AbstractKatharsisServlet
{
  private String path;
  private JsonServiceLocator locator;

  @Override
  public void init(ServletConfig servletConfig) throws ServletException
  {
    super.init(servletConfig);
    ApplicationContext ac = (ApplicationContext) servletConfig.getServletContext().getAttribute("applicationContext");
    locator = (JsonServiceLocator)ac.getBean("jsonServiceLocator");
    path = String.format( "%s/japi", servletConfig.getServletContext().getContextPath() );
  }

  @Override
  protected KatharsisInvokerBuilder createKatharsisInvokerBuilder()
  {
    return new KatharsisInvokerBuilder()
      .resourceSearchPackage( "org.openactive.PomReporter" )
      .resourceDefaultDomain( path )
      .jsonServiceLocator( locator );
  }
}
