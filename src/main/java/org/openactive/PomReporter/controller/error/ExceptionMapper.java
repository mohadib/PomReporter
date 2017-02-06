package org.openactive.PomReporter.controller.error;

import org.apache.log4j.Logger;
import org.openactive.PomReporter.exceptions.LockTimeoutException;
import org.openactive.PomReporter.service.impl.DeleteServiceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by mohadib on 2/5/17.
 */
public class ExceptionMapper
{
  private final static Logger LOG = Logger.getLogger(ExceptionMapper.class);

  @ExceptionHandler({Exception.class})
  private ResponseEntity<RestError> handleBadRequest(HttpServletRequest req, Exception exception)
  {
    LOG.error( exception.getMessage(), exception);

    RestError error = new RestError(500, "Internal Error");

    if (exception instanceof DataIntegrityViolationException)
    {
      error.setMsg(exception.getCause().getCause().getMessage());
    }
    else if (exception instanceof EntityNotFoundException)
    {
      error.setCode(404);
      error.setMsg("Entity not found");
    }
    else if( exception instanceof LockTimeoutException)
    {
      error.setCode(503);
      error.setMsg("Could not lock entity for deleting. Try again later.");
    }

    return new ResponseEntity<RestError>(error, HttpStatus.valueOf(error.getCode()));
  }
}
