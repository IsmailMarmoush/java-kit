package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;

public abstract class Evolver {

  /**
   * @param event A creational event
   * @return a new State with stateMeta as its meta value
   */
  public abstract State create(CreationEvent event);

  /**
   * @param currentState current State
   * @param event        evolution event to be applied upon current state
   * @return new state
   */
  public final State evolve(State currentState, EvolutionEvent event) {
    if (!currentState.stateId().equals(event.stateId())) {
      throw EvolutionException.invalidEventStateId(event, currentState.stateId());
    }
    return validatedEvolve(currentState, event);
  }

  /**
   * Called after stateId validated {@link evolve(State, EvolutionEvent)}
   *
   */
  protected abstract State validatedEvolve(State currentState, EvolutionEvent evolutionEvent);

}
