package com.vova.laba.exceptions;

public class ApiException extends RuntimeException {
  public ApiException(String message) {
    super(message);
  }
}
