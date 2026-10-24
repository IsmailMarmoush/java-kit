package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateMeta;

public interface Evolver {

  /**
   * @param creationEvent     A creational event
   * @param stateMeta the new State stateMeta
   * @return a new State with stateMeta as its meta value
   */
  State evolve(CreationEvent creationEvent, StateMeta stateMeta);

  /**
   * @param state     initial State
   * @param evolutionEvent     evolution event to be applied upon the initial state
   * @param stateMeta of
   * @return state with stateMeta as its meta value
   */
  State evolve(State state, EvolutionEvent evolutionEvent, StateMeta stateMeta);

  default State evolve(CreationEvent creationEvent) {
    return evolve(creationEvent, stateMeta(creationEvent));
  }

  default State evolve(State state, EvolutionEvent event) {
    if (!state.stateId().equals(event.stateId()))
      throw EvolutionException.invalidEventStateId(event, state.stateId());
    return evolve(state, event, stateMeta(event));
  }

  default StateMeta stateMeta(Event e) {
    return new StateMeta(e.meta().stateId());
  }
}
