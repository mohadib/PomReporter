package org.openactive.PomReporter.katharsis;

import io.katharsis.errorhandling.ErrorResponse;
import io.katharsis.errorhandling.mapper.ExceptionMapperProvider;

/**
 * Created by mohadib on 1/25/17.
 */
@ExceptionMapperProvider
public class ExceptionMapper implements io.katharsis.errorhandling.mapper.ExceptionMapper<Exception>
{
  @Override
  public ErrorResponse toErrorResponse(Exception exception)
  {
    return null;
  }

  @Override
  public Exception fromErrorResponse(ErrorResponse errorResponse)
  {
    return null;
  }

  @Override
  public boolean accepts(ErrorResponse errorResponse)
  {
    return true;
  }
  /*
  @Override
  public ErrorResponse toErrorResponse(Exception exception) {
    return ErrorResponse.builder()
      .setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500)
      .setSingleErrorData(ErrorData.builder()
        .setTitle("Wtf")
        .setId("42")
        .build())
      .build();
  }
  */
}