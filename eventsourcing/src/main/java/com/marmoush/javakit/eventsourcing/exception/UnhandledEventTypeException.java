package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.event.Event;

public final class UnhandledEventTypeException extends RuntimeException {
  private static final String MSG = "Unhandled Event: %s[%s] implementation";

  public static UnhandledEventTypeException of(Event event) {
    return new UnhandledEventTypeException(event);
  }

  private UnhandledEventTypeException(Event event) {
    super(MSG.formatted(event.getClass().getName(), event.meta()));
  }
}
