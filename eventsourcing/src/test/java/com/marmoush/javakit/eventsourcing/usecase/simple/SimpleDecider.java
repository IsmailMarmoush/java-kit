package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.event.CreationEventMeta;
import com.marmoush.javakit.eventsourcing.event.EvolutionEventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public class SimpleDecider extends Decider {
  public SimpleDecider(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public DecisionResult decide(CreationCommand command, CreationEventMeta eventMeta) {
    if (command instanceof CreateState) {
      return DecisionResult.of(new StateCreated(eventMeta));
    } else {
      throw DecisionException.unhandledCommandType(command);
    }
  }

  @Override
  public DecisionResult decide(State state, EvolutionCommand command, EvolutionEventMeta eventMeta) {
    if (state instanceof SimpleState) {
      if (command instanceof ChangeState) {
        return DecisionResult.success(new StateChanged(eventMeta));
      } else {
        throw DecisionException.unhandledCommandType(command);
      }
    } else {
      throw DecisionException.unhandledCommandType(command);
    }
  }
}
