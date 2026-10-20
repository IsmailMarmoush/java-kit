package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.core.id.Id;
import com.marmoush.javakit.eventsourcing.aggregate.Decider;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.aggregate.DecisionResult;
import com.marmoush.javakit.eventsourcing.aggregate.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.aggregate.error.MismatchingState;
import com.marmoush.javakit.eventsourcing.aggregate.exception.UnhandledCommandType;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.UnhandledStateType;

import java.util.function.Supplier;

public record SimpleDecider(Supplier<Id> idSupplier, Supplier<Long> timeSupplier) implements Decider {
  @Override
  public DecisionResult decide(Command command, EventMeta eventMeta) {
    if (command instanceof CreateState) {
      return DecisionResult.of(new StateCreated(eventMeta));
    } else {
      return DecisionFailure.of(UnhandledCommandType.of(command));
    }
  }

  @Override
  public DecisionResult decide(State state, Command command, EventMeta eventMeta)  {
    if (state instanceof SimpleState simpleState) {
      return decide(command, simpleState);
    } else {
      throw UnhandledStateType.of(state);
    }
  }

  private DecisionResult decide(Command command, SimpleState simpleState)  {
    return switch (command) {
      case CreateState createState -> DecisionFailure.of(MismatchingState.of(simpleState, createState));
      case ChangeState changeState -> new StateChanged(eventMeta(simpleState, changeState));
      default -> DecisionFailure.of(UnhandledCommandType.of(command));
    };
  }
}
