package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.aggregate.Evolver;
import com.marmoush.javakit.eventsourcing.aggregate.exception.MismatchingEventException;
import com.marmoush.javakit.eventsourcing.aggregate.exception.UnhandledEventTypeException;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateMeta;
import com.marmoush.javakit.eventsourcing.state.UnhandledStateType;

public record SimpleEvolver() implements Evolver {
  @Override
  public State evolve(Event event, StateMeta stateMeta) {
    if (event instanceof StateCreated) {
      return new SimpleState(stateMeta);
    } else {
      throw UnhandledEventTypeException.of(event);
    }
  }

  @Override
  public State evolve(State state, Event event, StateMeta stateMeta) {
    if (state instanceof SimpleState simpleState) {
      return switch (event) {
        case StateCreated stateCreated -> throw MismatchingEventException.of(simpleState, stateCreated);
        case StateChanged _ -> new SimpleState(stateMeta);
        default -> throw UnhandledStateType.of(simpleState);
      };
    } else {
      throw UnhandledStateType.of(state);
    }
  }
}
