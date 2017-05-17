package org.openactive.PomReporter.util;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.dao.VCSCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.openactive.PomReporter.domain.VCSCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Properties;

@Component
public class LoadData implements ApplicationListener<ContextRefreshedEvent>
{
   @Value("${environment}")
   private String environment;

   @Autowired
   private VCSCredenitalDAO svnCredRepo;

   @Autowired
   private ProjectDAO projectRepo;

   @Autowired
   private ProjectGroupDAO projectGroupRepo;

   @Value("${encryption.secret}")
   private String secret;

   @Value("${encryption.salt}")
   private String salt;

   @Override
   public void onApplicationEvent( ContextRefreshedEvent contextRefreshedEvent )
   {
      if ( environment.equals( "PROD" ) )
      {
         return;
      }

      if ( !contextRefreshedEvent.getApplicationContext().getDisplayName().contains( "Root" ) )
      {
         return;
      }

      try
      {
         Properties props = new Properties();
         props.load( new FileInputStream( "/home/jdavis/svn.props" ) );

         VCSCredential cred = new VCSCredential();
         cred.setName( "TLC_SVN" );
         cred.setUsername( props.getProperty( "svn.user" ) );
         String encPass = new EncryptionUtil().encrypt( props.getProperty( "svn.pass" ), secret.toCharArray(), salt.getBytes( "UTF-8" ) );
         cred.setPassword( encPass );
         svnCredRepo.save( cred );

         VCSCredential gitcred = new VCSCredential();
         gitcred.setName( "TLC_GIT" );
         gitcred.setUsername( props.getProperty( "git.user" ) );
         encPass = new EncryptionUtil().encrypt( props.getProperty( "git.pass" ), secret.toCharArray(), salt.getBytes( "UTF-8" ) );
         gitcred.setPassword( encPass );
         svnCredRepo.save( gitcred );


         ProjectGroup pg = new ProjectGroup();
         pg.setName( "Group 1" );
         pg.setDefault( true );
         pg = projectGroupRepo.save( pg );

         Project p1 = new Project();
			p1.setCredentials( svnCredRepo.findByName( "TLC_SVN" ) );
			p1.setName( "Record Keeper" );
			p1.setUrl( props.getProperty( "svn.url" ) );
			p1.setXpathExpression( "/project/version/text()" );
			p1.setProjectGroup( pg );
			projectRepo.save( p1 );

         Project gitP = new Project();
         gitP.setCredentials( svnCredRepo.findByName( "TLC_GIT" ) );
         gitP.setName( "IagoConnect" );
         gitP.setUrl( props.getProperty( "git.url" ) );
         gitP.setBranch( props.getProperty( "git.branch" ) );
         gitP.setXpathExpression( "/project/version/text()" );
         gitP.setProjectGroup( pg );
         projectRepo.save( gitP );

      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }
}
