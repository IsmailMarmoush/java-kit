package com.marmoush.javakit.eventsourcing.exception;

public class ESException extends RuntimeException {
  public ESException(String message) {
    super(message);
  }

  public ESException(String message, Throwable cause) {
    super(message, cause);
  }

  public ESException(Throwable cause) {
    super(cause);
  }
}
