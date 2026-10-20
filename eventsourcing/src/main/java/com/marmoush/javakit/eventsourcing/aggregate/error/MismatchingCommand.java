package com.marmoush.javakit.eventsourcing.aggregate.error;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.state.State;

public final class MismatchingCommand extends DefaultESDomainError {
  private static final String MESSAGE = "Mismatching command and state: Unable to apply State(%s) to Command(%s)";

  public static MismatchingCommand of(State state, Command command) {
    return new MismatchingCommand(state, command);
  }

  private MismatchingCommand(State state, Command command) {
    var message = MESSAGE.formatted(state.getClass().getName(), command.getClass().getName());
    super(message);
  }
}
