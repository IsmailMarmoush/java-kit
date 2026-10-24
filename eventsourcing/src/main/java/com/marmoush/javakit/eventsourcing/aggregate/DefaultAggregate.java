package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultAggregate implements Aggregate {
  private static final Logger log = LoggerFactory.getLogger(DefaultAggregate.class.getName());

  // Rules
  private final Decider decider;
  private final Evolver evolver;
  private final EventRepo eventRepo;

  public DefaultAggregate(Decider decider, Evolver evolver, EventRepo eventRepo) {
    // Rules
    this.decider = decider;
    this.evolver = evolver;
    this.eventRepo = eventRepo;
  }

  @Override
  public DecisionResult handle(Command command) {
    var result = process(command);
    if (result instanceof DecisionEvent(Event event)) {
      eventRepo.append(event);
      String msg = "Event[%s] appended".formatted(event.meta());
      log.debug(msg);
    }
    return result;
  }

  private DecisionResult process(Command command) {
    var events = eventRepo.fetch(command.meta().stateId());
    if (events.isEmpty()) {
      return decider.decide(command);
    }
    State state = evolver.evolve(events.getFirst());
    for (Event event : events) {
      state = evolver.evolve(state, event);
    }
    return decider.decide(state, command);
  }
}
