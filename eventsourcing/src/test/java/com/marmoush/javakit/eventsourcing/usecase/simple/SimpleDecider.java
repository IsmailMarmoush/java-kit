package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.error.MismatchingCommandError;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.UnhandledCommandTypeException;
import com.marmoush.javakit.eventsourcing.exception.UnhandledStateTypeException;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public record SimpleDecider(Supplier<EventId> eventIdSupplier, Supplier<Long> timeSupplier) implements Decider {
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
