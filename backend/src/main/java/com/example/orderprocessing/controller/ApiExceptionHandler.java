package com.example.orderprocessing.controller;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(NoSuchElementException.class)
  ResponseEntity<?> missing(NoSuchElementException exception) { return error(HttpStatus.NOT_FOUND, "NOT_FOUND", exception.getMessage()); }
  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<?> invalidState(IllegalStateException exception) { return error(HttpStatus.CONFLICT, "INVALID_STATE", exception.getMessage()); }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> invalid(MethodArgumentNotValidException exception) { return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed"); }
  private ResponseEntity<?> error(HttpStatus status, String code, String message) { return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now(), "status", status.value(), "error", code, "message", message == null ? code : message)); }
}
