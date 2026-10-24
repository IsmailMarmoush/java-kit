package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.state.State;

public final class EvolutionException extends ESException {

  public EvolutionException(String message) {
    super(message);
  }

  public EvolutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public EvolutionException(Throwable cause) {
    super(cause);
  }

  public static DecisionException unhandledEventType(Event event) {
    var message = "Unhandled event type: " + event.getClass().getName();
    return new DecisionException(message);
  }

  public static DecisionException unhandledStateType(State state) {
    var message = "Unhandled state type: " + state.getClass().getName();
    return new DecisionException(message);
  }

  public static EvolutionException invalidEventType(Event event, State state) {
    var message = "Event cannot be applied to state: stateType=%s, eventType=%s";
    return new EvolutionException(message.formatted(state.getClass().getName(), event.getClass().getName()));
  }

  public static EvolutionException invalidEventStateId(Event event, StateId expectedStateId) {
    var message = "Event stateId mismatch: expected=%s, actual=%s";
    return new EvolutionException(message.formatted(expectedStateId.value(), event.stateId().value()));
  }

  public static EvolutionException invalidEventVersion(Event event, long expectedVersion) {
    var message = "Invalid Event version: actual=%s, expected=%s".formatted(event.version(), expectedVersion);
    return new EvolutionException(message);
  }

}
