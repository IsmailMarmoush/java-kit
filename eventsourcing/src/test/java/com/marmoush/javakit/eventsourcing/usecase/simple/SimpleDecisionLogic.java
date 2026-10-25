package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.processor.DecisionLogic;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.state.State;

public class SimpleDecisionLogic implements DecisionLogic {

  @Override
  public DecisionResult decide(Command command, EventMeta eventMeta) {
    if (command instanceof CreateState) {
      return DecisionResult.of(new StateCreated(eventMeta));
    } else {
      throw DecisionException.unhandledCommandType(command);
    }
  }

  @Override
  public DecisionResult decide(State state, Command command, EventMeta eventMeta) {
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
