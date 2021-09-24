package com.ultimate.webservertask.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<Object> notFoundException(final HttpClientErrorException e, WebRequest request) {
    return handleExceptionInternal(e, e.getResponseBodyAsString(),
        new HttpHeaders(), e.getStatusCode(), request);
  }

}
