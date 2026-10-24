package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.exception.UnhandledEventTypeException;
import com.marmoush.javakit.eventsourcing.exception.UnhandledStateTypeException;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateMeta;

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
        case StateCreated stateCreated -> throw EvolutionException.of(simpleState, stateCreated);
        case StateChanged _ -> new SimpleState(stateMeta);
        default -> throw UnhandledStateTypeException.of(simpleState);
      };
    } else {
      throw UnhandledStateTypeException.of(state);
    }
  }
}
