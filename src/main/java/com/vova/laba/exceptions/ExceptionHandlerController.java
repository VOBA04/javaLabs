package com.vova.laba.exceptions;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundExcepcion.class)
  public ResponseEntity<ExceptionDetails> notFound(
      NotFoundExcepcion excepcion, WebRequest request) {
    ExceptionDetails details =
        new ExceptionDetails(new Date(), excepcion.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionDetails> badRequest(
      BadRequestException exception, WebRequest request) {
    ExceptionDetails details =
        new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ExceptionDetails> apiException(ApiException exception, WebRequest request) {
    ExceptionDetails details =
        new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
