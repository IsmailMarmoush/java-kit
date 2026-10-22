package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.error.DuplicateCommand;
import com.marmoush.javakit.eventsourcing.error.MismatchingCommandStateIdError;
import com.marmoush.javakit.eventsourcing.exception.MismatchingEventStateIdException;
import com.marmoush.javakit.eventsourcing.exception.MismatchingEventVersionException;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.id.StateId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

class SyncAggregate implements Aggregate {
  private static final Logger log = LoggerFactory.getLogger(SyncAggregate.class.getName());

  private final StateId stateId;
  // Rules
  private final Decider decider;
  private final Evolver evolver;
  private final EventRepo eventRepo;

  // State
  private final AtomicReference<State> stateRef;
  private final Set<CommandId> processedCommands;
  private final Set<EventId> sagaSources;

  public SyncAggregate(StateId stateId, Decider decider, Evolver evolver, EventRepo eventRepo) {
    this.stateId = stateId;
    // Rules
    this.decider = decider;
    this.evolver = evolver;
    this.eventRepo = eventRepo;

    // State
    this.stateRef = new AtomicReference<>();
    this.processedCommands = new HashSet<>();
    this.sagaSources = new HashSet<>();
  }

  @Override
  public StateId stateId() {
    return this.stateId;
  }

  @Override
  public synchronized DecisionResult handle(Command command) {
    initialize();
    // Validations should be after initialization
    if (isDuplicate(command)) {
      return DecisionResult.failure(DuplicateCommand.of(command));
    }
    // Return failure if it doesn't match state
    if (!stateId().equals(command.stateId())) {
      return DecisionResult.failure(MismatchingCommandStateIdError.of(stateId(), command));
    }

    var result = decide(command);
    if (result instanceof DecisionEvent(Event event)) {
      eventRepo.append(event);
      String msg = "Event[%s] appended".formatted(event.meta());
      log.debug(msg);
      command.meta().sagaSource().ifPresent(sagaSources::add);
    }
    return result;
  }

  private DecisionResult decide(Command command) {
    DecisionResult result;
    if (stateRef.get() == null) {
      // If still no state, expect a creation command
      result = decider.decide(command);
    } else {
      result = decider.decide(stateRef.get(), command);
    }
    return result;
  }

  /**
   * Initialize if no initial state
   */
  private void initialize() {
    if (stateRef.get() == null) {
      eventRepo.fetch(stateId()).forEach(this::evolve);
    }
  }

  void evolve(Event event) {
    validate(event);
    State currentState = stateRef.get();
    State newState;
    if (isInitializerEvent(event)) {
      newState = evolver.evolve(event);
    } else {
      newState = evolver.evolve(currentState, event);
    }
    stateRef.set(newState);
    String msg = "New State [%s]".formatted(newState.meta());
    log.debug(msg);
    processedCommands.add(event.meta().commandId());
  }

  boolean isDuplicate(Command command) {
    var alreadyProcessedCmd = processedCommands.contains(command.meta().commandId());
    var alreadyProcessedSagaCmd = command.meta().sagaSource().map(sagaSources::contains).orElse(false);
    return alreadyProcessedCmd || alreadyProcessedSagaCmd;
  }

  void validate(Event event) {
    // Check if matches stateId
    if (!stateId().equals(event.stateId())) {
      throw MismatchingEventStateIdException.of(stateId(), event);
    }
    // Check if matches expected version
    long expectedVersion = Optional.ofNullable(stateRef.get()).map(State::version).map(v -> v + 1).orElse(0L);
    if (event.version() != expectedVersion) {
      throw MismatchingEventVersionException.of(event, expectedVersion);
    }
  }

  boolean isInitializerEvent(Event event) {
    return event.version() == 0;
  }
}
