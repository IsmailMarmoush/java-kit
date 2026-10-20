package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;

public final class MismatchingEventVersion extends RuntimeException {
  private static final String MSG = "Can't evolve State with unexpected version %s of Event(eventMeta=%s)";

  public static MismatchingEventVersion of(Event event, long expectedVersion) {
    return new MismatchingEventVersion(event, expectedVersion);
  }

  private MismatchingEventVersion(Event event, long expectedVersion) {
    super(MSG.formatted(expectedVersion, event.meta()));
  }
}
