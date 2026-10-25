package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.State;

public interface EvolutionLogic {
  State evolve(Event event);

  State evolve(State state, Event event);
}
