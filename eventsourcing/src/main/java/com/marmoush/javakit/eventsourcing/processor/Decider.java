package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateId;

import java.util.function.Supplier;

public abstract class Decider {
  private final Supplier<Id> idSupplier;

  public Decider(Supplier<Id> idSupplier) {
    this.idSupplier = idSupplier;
  }

  /**
   * @param command the incoming command
   * @return a new Event with eventMeta as its value
   */
  public abstract DecisionResult decide(Command command);

  /**
   * @param state   the initial state
   * @param command the incoming command
   * @return a CommandResult with Event(eventMeta) as its value if accepted
   *
   */
  public final DecisionResult decide(State state, Command command) {
    if (!state.meta().stateId().equals(command.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(command, state.meta().stateId());
    }
    return validatedDecide(state, command);
  }

  /**
   * Called after stateId validated {@link #decide(State, Command)}
   */
  protected abstract DecisionResult validatedDecide(State state, Command command);

  /**
   * Helper function to create EventMeta from Command
   *
   * @param command incoming Command
   * @return EventMeta from Command
   */
  protected EventMeta eventMeta(Command command) {
    return EventMeta.builder()
                    .eventId(EventId.of(idSupplier().get()))
                    .operationType(command.meta().operationType())
                    .version(0)
                    .stateId(StateId.of(idSupplier().get()))
                    .commandId(command.meta().commandId())
                    .sagaSource(command.meta().sagaSource().orElse(null))
                    .build();
  }

  /**
   * Helper function to create EventMeta from Command
   *
   * @param state   current State
   * @param command incoming Command
   * @return EventMeta from Command
   */
  protected EventMeta eventMeta(State state, Command command) {
    if (!state.meta().stateId().equals(command.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(command, state.meta().stateId());
    }
    return EventMeta.builder()
                    .eventId(EventId.of(idSupplier().get()))
                    .operationType(command.meta().operationType())
                    .version(state.meta().version() + 1)
                    .stateId(StateId.of(idSupplier().get()))
                    .commandId(command.meta().commandId())
                    .sagaSource(command.meta().sagaSource().orElse(null))
                    .build();
  }

  public Supplier<Id> idSupplier() {
    return idSupplier;
  }
}
