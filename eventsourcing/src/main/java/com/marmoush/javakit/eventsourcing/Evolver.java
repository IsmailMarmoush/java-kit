package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;

public abstract class Evolver {

  /**
   * @param creationEvent A creational event
   * @return a new State with stateMeta as its meta value
   */
  public abstract State create(CreationEvent creationEvent);

  /**
   * @param currentState   current State
   * @param evolutionEvent evolution event to be applied upon current state
   * @return new state
   */
  public State evolve(State currentState, EvolutionEvent evolutionEvent) {
    if (!currentState.stateId().equals(evolutionEvent.stateId())) {
      throw EvolutionException.invalidEventStateId(evolutionEvent, currentState.stateId());
    }
    return validStateEvolve(currentState, evolutionEvent);
  }

  /**
   * Called after stateId validated {@link evolve(State, EvolutionEvent)}
   *
   */
  protected abstract State validStateEvolve(State currentState, EvolutionEvent evolutionEvent);

}
