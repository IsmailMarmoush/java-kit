package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
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
    var result = switch (command) {
      case CreationCommand cc -> handle(cc);
      case EvolutionCommand ec -> handle(ec);
      default -> throw DecisionException.unhandledCommandType(command);
    };
    if (result instanceof DecisionEvent(Event event)) {
      eventRepo.append(event);
      String msg = "Event[%s] appended".formatted(event.meta());
      log.debug(msg);
    }
    return result;
  }

  public DecisionResult handle(CreationCommand creationCommand) {
    return decider.decide(creationCommand);
  }

  public DecisionResult handle(EvolutionCommand evolutionCommand) {
    var events = eventRepo.fetch(evolutionCommand.meta().stateId());
    State state = null;
    for (Event event : events) {
      state = switch (event) {
        case CreationEvent creationEvent -> evolve(creationEvent);
        case EvolutionEvent evolutionEvent -> evolve(state, evolutionEvent);
        default -> throw EvolutionException.unhandledEventType(event);
      };
    }
    return decider.decide(state, evolutionCommand);
  }

  State evolve(CreationEvent creationEvent) {
    return evolver.create(creationEvent);
  }

  State evolve(State state, EvolutionEvent evolutionEvent) {
    return evolver.evolve(state, evolutionEvent);
  }
}
