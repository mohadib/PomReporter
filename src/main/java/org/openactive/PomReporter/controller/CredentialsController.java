package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.controller.error.RestError;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jdavis on 1/31/17.
 */
@RestController
@RequestMapping("/credentials")
public class CredentialsController
{
	@Autowired
	private SvnCredenitalDAO credenitalDAO;

	@GetMapping
	public List<SvnCredential> getAll()
	{
		return credenitalDAO.findAllAndFetchProjectsEagerly();
	}

	@GetMapping("/{id}")
	public SvnCredential get( @PathVariable("id") Integer id )
	{
		return credenitalDAO.findByIdAndFetchProjectsEagerly( id );
	}

	@PatchMapping
	public SvnCredential patch( @RequestBody SvnCredential credential )
	{
		SvnCredential orig = credenitalDAO.findByIdAndFetchProjectsEagerly( credential.getId() );
		orig.setName( credential.getName() );
		orig.setProtocol( credential.getProtocol() );
		orig.setPort( credential.getPort() );
		orig.setHost( credential.getHost() );
		orig.setUsername( credential.getUsername() );
		orig.setPassword( credential.getPassword() );
		return credenitalDAO.save( orig );
	}

	@PostMapping
	public SvnCredential post( @RequestBody SvnCredential credential )
	{
		return credenitalDAO.save( credential );
	}

	@ExceptionHandler( { Exception.class } )
	private ResponseEntity<RestError> handleBadRequest( HttpServletRequest req, Exception exception )
	{
		RestError error = new RestError( 500, "Internal Error" );

		if( exception instanceof DataIntegrityViolationException )
		{
			error.setMsg( exception.getCause().getCause().getMessage()  );
		}

		return new ResponseEntity<RestError>( error, HttpStatus.valueOf( error.getCode() ) );
	}
}
