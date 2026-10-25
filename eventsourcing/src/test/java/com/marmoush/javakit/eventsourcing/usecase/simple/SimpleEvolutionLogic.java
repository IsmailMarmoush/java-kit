package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.processor.EvolutionLogic;
import com.marmoush.javakit.eventsourcing.state.State;

public class SimpleEvolutionLogic implements EvolutionLogic {
  @Override
  public State evolve(Event event) {
    if (event instanceof StateCreated) {
      return new SimpleState(event.meta());
    } else {
      throw EvolutionException.unhandledEventType(event);
    }
  }

  @Override
  public State evolve(State state, Event event) {
    if (state instanceof SimpleState simpleState) {
      if (event instanceof StateChanged) {
        return simpleState.withMeta(event.meta());
      } else {
        throw EvolutionException.unhandledEventType(event);
      }
    } else {
      throw EvolutionException.unhandledStateType(state);
    }
  }
}
