package com.ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ApiError> build(HttpStatus status, String msg, HttpServletRequest req) {
    return ResponseEntity.status(status).body(ApiError.builder()
        .timestamp(Instant.now()).status(status.value()).error(status.getReasonPhrase())
        .message(msg).path(req.getRequestURI()).build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> bad(BadRequestException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> nf(NotFoundException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> invalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField()+": "+fe.getDefaultMessage()).collect(Collectors.joining(", "));
    return build(HttpStatus.BAD_REQUEST, msg, req);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiError> rse(ResponseStatusException ex, HttpServletRequest req) {
    return build(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), req);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> other(Exception ex, HttpServletRequest req) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req);
  }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
