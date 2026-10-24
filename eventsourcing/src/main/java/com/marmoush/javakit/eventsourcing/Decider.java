package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
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
   * @param creationCommand   the incoming command
   * @param creationEventMeta the new EventMeta
   * @return a new Event with eventMeta as its value
   */
  protected abstract DecisionResult decide(CreationCommand creationCommand, EventMeta creationEventMeta);

  /**
   * @param state              the initial state
   * @param evolutionCommand   the incoming command
   * @param evolutionEventMeta the new EventMeta
   * @return a CommandResult with Event(eventMeta) as its value if accepted
   *
   */
  protected abstract DecisionResult decide(State state,
                                           EvolutionCommand evolutionCommand,
                                           EventMeta evolutionEventMeta);

  public DecisionResult decide(CreationCommand creationCommand) {
    return decide(creationCommand, eventMeta(creationCommand));
  }

  public DecisionResult decide(State state, EvolutionCommand evolutionCommand) {
    return decide(state, evolutionCommand, eventMeta(state, evolutionCommand));
  }

  private EventMeta eventMeta(Command command) {
    return new EventMeta(EventId.of(idSupplier().get()),
                         0,
                         StateId.of(idSupplier().get()),
                         command.meta().commandId(),
                         command.meta().sagaSource().orElse(null));
  }

  private EventMeta eventMeta(State state, EvolutionCommand evolutionCommand) {
    if (!state.meta().stateId().equals(evolutionCommand.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(evolutionCommand, state.meta().stateId());
    }
    return new EventMeta(EventId.of(idSupplier().get()),
                         state.meta().version() + 1,
                         state.meta().stateId(),
                         evolutionCommand.meta().commandId(),
                         evolutionCommand.meta().sagaSource().orElse(null));
  }

  public Supplier<Id> idSupplier() {
    return idSupplier;
  }
}
