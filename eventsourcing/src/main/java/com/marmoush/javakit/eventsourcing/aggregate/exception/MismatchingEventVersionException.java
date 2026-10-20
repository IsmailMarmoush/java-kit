package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;

public final class MismatchingEventVersionException extends IllegalStateException {
  private static final String MSG = "Can't evolve State with unexpected version %s of Event(eventMeta=%s)";

  public static MismatchingEventVersionException of(Event event, long expectedVersion) {
    return new MismatchingEventVersionException(event, expectedVersion);
  }

  private MismatchingEventVersionException(Event event, long expectedVersion) {
    super(MSG.formatted(expectedVersion, event.meta()));
  }
}
