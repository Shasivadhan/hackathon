package com.ecommerce.exception;
public class BadRequestException extends RuntimeException {
  public BadRequestException(String m) { super(m); }
}
