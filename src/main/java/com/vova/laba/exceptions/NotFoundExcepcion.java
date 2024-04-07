package com.vova.laba.exceptions;

public class NotFoundExcepcion extends RuntimeException {

  public NotFoundExcepcion(String message, Long id) {
    super(message + Long.toString(id));
  }

  public NotFoundExcepcion(String message) {
    super(message);
  }
}
