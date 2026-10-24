package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateId;

public class DecisionException extends ESException {

  public DecisionException(String message) {
    super(message);
  }

  public DecisionException(String message, Throwable cause) {
    super(message, cause);
  }

  public DecisionException(Throwable cause) {
    super(cause);
  }

  public static DecisionException unhandledCommandType(Command command) {
    var message = "Unhandled command type: " + command.getClass().getName();
    return new DecisionException(message);
  }

  public static DecisionException invalidCommand(Command command, State state) {
    var message = "Command cannot be applied to state: stateType=%s, commandType=%s";
    return new DecisionException(message.formatted(state.getClass().getName(), command.getClass().getName()));
  }

  public static DecisionException invalidCommandStateId(Command command, StateId expectedStateId) {
    var message = "Command stateId mismatch: expected=%s, actual=%s";
    return new DecisionException(message.formatted(expectedStateId.value(), command.meta().stateId().value()));
  }
}
