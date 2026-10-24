package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.state.State;

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
    if (!state.stateId().equals(command.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(command, state.stateId());
    }
    return validatedDecide(state, command);
  }

  /**
   * Called after stateId validated {@link #decide(State, Command)}
   */
  protected abstract DecisionResult validatedDecide(State state, Command command);

  /**
   * Helper function to create EventMeta from CreationCommand
   *
   * @param command incoming CreationCommand
   * @return EventMeta from CreationCommand
   */
  protected EventMeta eventMeta(Command command) {
    EventMeta.builder()
             .eventId(EventId.of(idSupplier().get()))
             .operationType(command.meta().operationType())
             .version(0)
             .stateId(StateId.of(idSupplier().get()))
             .commandId(command.meta().commandId())
             .sagaSource(command.meta().sagaSource().orElse(null))
             .build();
    return new EventMeta(EventId.of(idSupplier().get()),
                         0,
                         StateId.of(idSupplier().get()),
                         command.meta().commandId(),
                         command.meta().sagaSource().orElse(null));
  }

  /**
   * Helper function to create EventMeta from CreationCommand
   *
   * @param state   current State
   * @param command incoming CreationCommand
   * @return EventMeta from CreationCommand
   */
  protected EventMeta eventMeta(State state, Command command) {
    if (!state.meta().stateId().equals(command.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(command, state.meta().stateId());
    }
    return new EventMeta(EventId.of(idSupplier().get()),
                         state.meta().version() + 1,
                         state.meta().stateId(),
                         command.meta().commandId(),
                         command.meta().sagaSource().orElse(null));
  }

  public Supplier<Id> idSupplier() {
    return idSupplier;
  }
}
