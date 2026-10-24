package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateMeta;

public record SimpleEvolver() implements Evolver {
  @Override
  public State evolve(CreationEvent creationEvent, StateMeta stateMeta) {
    if (creationEvent instanceof StateCreated) {
      return new SimpleState(stateMeta);
    } else {
      throw EvolutionException.unhandledEventType(creationEvent);
    }
  }

  @Override
  public State evolve(State state, EvolutionEvent evolutionEvent, StateMeta stateMeta) {
    if (state instanceof SimpleState simpleState) {
      if (evolutionEvent instanceof StateChanged) {
        return new SimpleState(stateMeta);
      } else {
        throw EvolutionException.unhandledEventType(evolutionEvent);
      }
    } else {
      throw EvolutionException.unhandledStateType(state);
    }
  }
}
