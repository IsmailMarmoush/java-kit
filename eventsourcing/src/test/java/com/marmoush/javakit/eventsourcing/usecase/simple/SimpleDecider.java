package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public class SimpleDecider extends Decider {
  public SimpleDecider(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public DecisionResult decide(Command command) {
    if (command instanceof CreateState) {
      var eventMeta = this.eventMeta(command);
      return DecisionResult.of(new StateCreated(eventMeta));
    } else {
      throw DecisionException.unhandledCommandType(command);
    }
  }

  @Override
  protected DecisionResult validatedDecide(State state, Command command) {
    if (state instanceof SimpleState) {
      if (command instanceof ChangeState) {
        var eventMeta = this.eventMeta(state, command);
        return DecisionResult.success(new StateChanged(eventMeta));
      } else {
        throw DecisionException.unhandledCommandType(command);
      }
    } else {
      throw DecisionException.unhandledCommandType(command);
    }
  }
}
