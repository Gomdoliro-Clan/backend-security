package com.acme.procure.common;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiErrors {
  private static final Logger LOG = LoggerFactory.getLogger(ApiErrors.class);

  @ExceptionHandler(ApiException.class)
  ResponseEntity<?> api(ApiException e) {
    return ResponseEntity.status(e.status()).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  ResponseEntity<?> validation(Exception e) {
    return ResponseEntity.badRequest().body(Map.of("error", "Invalid request"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<?> access(Exception e) {
    return ResponseEntity.status(403).body(Map.of("error", "Operation is not permitted"));
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<?> unexpected(Exception e) {
    LOG.warn("Request failed: {}", e.getClass().getSimpleName());
    return ResponseEntity.internalServerError()
        .body(Map.of("error", "Request could not be completed"));
  }
}
