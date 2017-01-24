package org.openactive.PomReporter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Created by jdavis on 1/24/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = SvnServiceImplTest.Config.class )
public class SvnServiceImplTest
{
	@Autowired
	private SvnServiceImpl service;

	@Test
	public void testCheckout() throws Exception
	{
		Properties props = new Properties(  );
		props.load( new FileInputStream( "/home/jdavis/svn.props" ) );

		SvnCredential creds = new SvnCredential();
		creds.setName( "test" );
		creds.setUsername( props.getProperty( "user" ) );
		creds.setPassword( props.getProperty( "pass" ) );
		creds.setHost( props.getProperty( "host" ) );
		creds.setPort( 80 );
		creds.setProtocol( "http" );

		Project project = new Project();
		project.setName( "ls2p" );
		project.setPath( props.get( "path" ).toString() );
		project.setXpathExpression( "/project/version/text()" );
		project.setCredentials( creds );

		service.checkout( project );
	}


	@Configuration
	public static class Config
	{
		@Bean
		public SvnServiceImpl SvnServiceImpl()
		{
			return new SvnServiceImpl();
		}

		@Bean
		public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer()
		{
			PropertyPlaceholderConfigurer config = new PropertyPlaceholderConfigurer();
			Properties props = new Properties();
			props.put( "svn.checkout.base.path.fileName.allowed.chars" , "a-zA-Z0-9_@");
			props.put( "svn.checkout.base.path" , "/tmp/projects");
			config.setProperties( props );
			return config;
		}

	}
}
