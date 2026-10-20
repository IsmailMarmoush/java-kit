package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.StateId;

public final class MismatchingEventStateId extends RuntimeException {
  private static final String MESSAGE = "Mismatching Event and state Ids: State(StateId=%s) != Event(StateId=%s)";

  public static MismatchingEventStateId of(StateId stateId, Event event) {
    return new MismatchingEventStateId(stateId, event);
  }

  private MismatchingEventStateId(StateId stateId, Event event) {
    var message = MESSAGE.formatted(stateId.value(), event.stateId().value());
    super(message);
  }
}
