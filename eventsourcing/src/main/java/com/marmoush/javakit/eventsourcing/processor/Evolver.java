package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;

public class Evolver {
  private final EvolutionLogic evolutionLogic;

  public Evolver(EvolutionLogic evolutionLogic) {
    this.evolutionLogic = evolutionLogic;
  }

  /**
   * @param event A creational event
   * @return a new State with stateMeta as its meta value
   */
  public final State evolve(Event event) {
    return this.evolutionLogic.evolve(event);
  }

  /**
   * @param state current State
   * @param event evolution event to be applied upon current state
   * @return new state
   */
  public final State evolve(State state, Event event) {
    validate(state, event);
    return this.evolutionLogic.evolve(state, event);
  }

  private static void validate(State currentState, Event event) {
    if (!currentState.meta().stateId().equals(event.meta().stateId())) {
      throw EvolutionException.invalidEventStateId(event, currentState.meta().stateId());
    }
  }
}
