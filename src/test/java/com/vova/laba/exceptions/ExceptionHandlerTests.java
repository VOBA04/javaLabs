package com.vova.laba.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTests {

  @InjectMocks private ExceptionHandlerController exceptionHandler;

  @Test
  void testHandleBadRequestException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    WebRequest webRequest = new ServletWebRequest(request);
    ResponseEntity<ExceptionDetails> result =
        exceptionHandler.badRequest(new BadRequestException("Test"), webRequest);

    assertEquals("Test", result.getBody().getMessage());
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testHandlerNotFoundException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    WebRequest webRequest = new ServletWebRequest(request);
    ResponseEntity<ExceptionDetails> result =
        exceptionHandler.notFound(new NotFoundExcepcion("Test"), webRequest);
    assertEquals("Test", result.getBody().getMessage());
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void testHandlerApiException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    WebRequest webRequest = new ServletWebRequest(request);
    ResponseEntity<ExceptionDetails> result =
        exceptionHandler.apiException(new ApiException("Test"), webRequest);
    assertEquals("Test", result.getBody().getMessage());
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
  }

  @Test
  void testHandlerInternalServiceException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    WebRequest webRequest = new ServletWebRequest(request);
    ResponseEntity<ExceptionDetails> result =
        exceptionHandler.internalServiceException(new Exception("Test"), webRequest);
    assertEquals("Test", result.getBody().getMessage());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }
}
