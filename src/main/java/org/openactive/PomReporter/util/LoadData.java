package org.openactive.PomReporter.util;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Created by mohadib on 1/23/17.
 */
@Component
public class LoadData implements ApplicationListener<ContextRefreshedEvent>
{
  @Value("${environment}")
  private String environment;

  @Autowired
  private SvnCredenitalDAO svnCredRepo;

  @Autowired
  private ProjectDAO projectRepo;

  @Autowired
  private ProjectGroupDAO projectGroupRepo;


  @Override
  public void onApplicationEvent( ContextRefreshedEvent contextRefreshedEvent )
  {
    if( environment.equals("PROD"))
    {
      return;
    }

    if( contextRefreshedEvent.getApplicationContext().getDisplayName().indexOf( "Root" ) == -1 )
    {
      return;
    }

    try
    {
        Properties props = new Properties();
        props.load( new FileInputStream( "/home/mohadib/svn.props" ) );

        SvnCredential cred = new SvnCredential();
        cred.setName( "TLC" );
        cred.setHost( props.getProperty( "host" ) );
        cred.setUsername( props.getProperty( "user" ) );
        cred.setPassword( props.getProperty( "pass" ) );
        cred.setProtocol( "http" );
        cred.setPort( 80 );
        cred = svnCredRepo.save( cred );

        ProjectGroup pg = new ProjectGroup();
        pg.setName( "Group 1" );
        pg = projectGroupRepo.save( pg );

        Project p1 = new Project();
        p1.setCredentials( svnCredRepo.findByName( "TLC" ) );
        p1.setName( "LS2P Trunk" );
        p1.setPath( props.getProperty( "path" ) );
        p1.setXpathExpression( "/project/version/text()" );
        p1.setProjectGroup( pg );
        projectRepo.save( p1 );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }
  }
}
