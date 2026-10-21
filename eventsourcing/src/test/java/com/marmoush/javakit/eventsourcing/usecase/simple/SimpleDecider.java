package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.core.id.Id;
import com.marmoush.javakit.eventsourcing.aggregate.Decider;
import com.marmoush.javakit.eventsourcing.aggregate.DecisionResult;
import com.marmoush.javakit.eventsourcing.aggregate.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.aggregate.error.MismatchingCommandError;
import com.marmoush.javakit.eventsourcing.aggregate.exception.UnhandledCommandTypeException;
import com.marmoush.javakit.eventsourcing.aggregate.exception.UnhandledStateTypeException;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public record SimpleDecider(Supplier<Id> idSupplier, Supplier<Long> timeSupplier) implements Decider {
  @Override
  public DecisionResult decide(Command command, EventMeta eventMeta) {
    if (command instanceof CreateState) {
      return DecisionResult.of(new StateCreated(eventMeta));
    } else {
      throw UnhandledCommandTypeException.of(command);
    }
  }

  @Override
  public DecisionResult decide(State state, Command command, EventMeta eventMeta) {
    if (state instanceof SimpleState simpleState) {
      return decide(command, simpleState, eventMeta);
    } else {
      throw UnhandledStateTypeException.of(state);
    }
  }

  private DecisionResult decide(Command command, SimpleState simpleState, EventMeta eventMeta) {
    return switch (command) {
      case CreateState createState -> DecisionFailure.of(MismatchingCommandError.of(simpleState, createState));
      case ChangeState _ -> DecisionResult.success(new StateChanged(eventMeta));
      default -> throw UnhandledCommandTypeException.of(command);
    };
  }
}
