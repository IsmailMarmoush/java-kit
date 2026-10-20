package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.State;

public final class MismatchingEvent extends IllegalStateException {
  private static final String MESSAGE = "Mismatching event and state: Unable to apply State(%s) to Event(%s)";

  public static MismatchingEvent of(State state, Event event) {
    return new MismatchingEvent(state, event);
  }

  private MismatchingEvent(State state, Event event) {
    var message = MESSAGE.formatted(state.getClass().getName(), event.getClass().getName());
    super(message);
  }
}
