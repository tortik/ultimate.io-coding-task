package com.ultimate.webservertask.http.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<Object> notFoundException(final HttpClientErrorException e, WebRequest request) {
    log.info("Got Exception:", e);
    return handleExceptionInternal(e, e.getResponseBodyAsString(),
        new HttpHeaders(), e.getStatusCode(), request);
  }

}
