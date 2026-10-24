package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.event.CreationEventMeta;
import com.marmoush.javakit.eventsourcing.event.EvolutionEventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public interface Decider {
  Supplier<Id> idSupplier();

  /**
   * @param creationCommand   the incoming command
   * @param creationEventMeta the new EventMeta
   * @return a new Event with eventMeta as its value
   */
  DecisionResult decide(CreationCommand creationCommand, CreationEventMeta creationEventMeta);

  /**
   * @param state              the initial state
   * @param evolutionCommand   the incoming command
   * @param evolutionEventMeta the new EventMeta
   * @return a CommandResult with Event(eventMeta) as its value if accepted
   *
   */
  DecisionResult decide(State state, EvolutionCommand evolutionCommand, EvolutionEventMeta evolutionEventMeta);

  //
  // CreationCommand handling
  //

  default DecisionResult decide(CreationCommand creationCommand) {
    return decide(creationCommand, eventMeta(creationCommand));
  }

  default CreationEventMeta eventMeta(Command command) {
    return new CreationEventMeta(EventId.of(idSupplier().get()),
                                 StateId.of(idSupplier().get()),
                                 command.meta().commandId(),
                                 command.meta().sagaSource().orElse(null));
  }

  //
  // EvolutionCommand handling
  //

  default DecisionResult decide(State state, EvolutionCommand evolutionCommand, long eventVersion) {
    return decide(state, evolutionCommand, eventMeta(state, evolutionCommand, eventVersion));
  }

  default EvolutionEventMeta eventMeta(State state, EvolutionCommand evolutionCommand, long eventVersion) {
    if (!state.meta().stateId().equals(evolutionCommand.meta().stateId())) {
      throw DecisionException.invalidCommandStateId(evolutionCommand, state.meta().stateId());
    }
    return new EvolutionEventMeta(EventId.of(idSupplier().get()),
                                  eventVersion,
                                  state.meta().stateId(),
                                  evolutionCommand.meta().commandId(),
                                  evolutionCommand.meta().sagaSource().orElse(null));
  }
}
