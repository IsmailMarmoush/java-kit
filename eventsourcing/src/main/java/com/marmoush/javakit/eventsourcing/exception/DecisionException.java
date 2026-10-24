package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.state.State;

public final class DecisionException extends ESException {

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

  public static DecisionException invalidEvolutionCommand(EvolutionCommand command, State state) {
    var message = "Command cannot be applied to state: stateType=%s, commandType=%s";
    return new DecisionException(message.formatted(state.getClass().getName(), command.getClass().getName()));
  }

  public static DecisionException invalidCommandStateId(EvolutionCommand evolutionCommand, StateId expectedStateId) {
    var message = "Command stateId mismatch: expected=%s, actual=%s";
    return new DecisionException(message.formatted(expectedStateId.value(), evolutionCommand.stateId().value()));
  }
}
