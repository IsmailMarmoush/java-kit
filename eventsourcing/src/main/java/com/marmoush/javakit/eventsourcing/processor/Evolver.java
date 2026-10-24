package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;

public abstract class Evolver {

  /**
   * @param event A creational event
   * @return a new State with stateMeta as its meta value
   */
  public abstract State evolve(Event event);

  /**
   * @param currentState current State
   * @param event        evolution event to be applied upon current state
   * @return new state
   */
  public final State evolve(State currentState, Event event) {
    if (!currentState.meta().stateId().equals(event.meta().stateId())) {
      throw EvolutionException.invalidEventStateId(event, currentState.meta().stateId());
    }
    return validatedEvolve(currentState, event);
  }

  /**
   * Called after stateId validated {@link evolve(State, Event)}
   *
   */
  protected abstract State validatedEvolve(State currentState, Event event);

}
