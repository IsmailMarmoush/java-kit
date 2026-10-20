package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.State;

public final class MismatchingEventException extends IllegalStateException {
  private static final String MESSAGE = "Mismatching event and state: Unable to apply State(%s) to Event(%s)";

  public static MismatchingEventException of(State state, Event event) {
    return new MismatchingEventException(state, event);
  }

  private MismatchingEventException(State state, Event event) {
    var message = MESSAGE.formatted(state.getClass().getName(), event.getClass().getName());
    super(message);
  }
}
