package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.id.StateId;

public final class MismatchingEventStateIdException extends IllegalStateException {
  private static final String MESSAGE = "Mismatching Event and state Ids: State(StateId=%s) != Event(StateId=%s)";

  public static MismatchingEventStateIdException of(StateId stateId, Event event) {
    return new MismatchingEventStateIdException(stateId, event);
  }

  private MismatchingEventStateIdException(StateId stateId, Event event) {
    var message = MESSAGE.formatted(stateId.value(), event.stateId().value());
    super(message);
  }
}
