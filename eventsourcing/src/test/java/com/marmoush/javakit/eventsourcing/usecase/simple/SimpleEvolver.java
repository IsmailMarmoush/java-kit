package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;

public class SimpleEvolver extends Evolver {
  @Override
  public State create(CreationEvent creationEvent) {
    if (creationEvent instanceof StateCreated) {
      return new SimpleState(creationEvent.meta());
    } else {
      throw EvolutionException.unhandledEventType(creationEvent);
    }
  }

  @Override
  public State validatedEvolve(State state, EvolutionEvent evolutionEvent) {
    if (state instanceof SimpleState simpleState) {
      if (evolutionEvent instanceof StateChanged) {
        return simpleState.withMeta(evolutionEvent.meta());
      } else {
        throw EvolutionException.unhandledEventType(evolutionEvent);
      }
    } else {
      throw EvolutionException.unhandledStateType(state);
    }
  }
}
