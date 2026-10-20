package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;

public final class UnhandledEventType extends RuntimeException {
  private static final String MSG = "Unhandled Event: %s[%s] implementation";

  public static UnhandledEventType of(Event event) {
    return new UnhandledEventType(event);
  }

  private UnhandledEventType(Event event) {
    super(MSG.formatted(event.getClass().getName(), event.meta()));
  }
}
