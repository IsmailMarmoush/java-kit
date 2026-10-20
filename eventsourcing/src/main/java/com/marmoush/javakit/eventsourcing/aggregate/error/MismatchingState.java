package com.marmoush.javakit.eventsourcing.aggregate.error;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.state.State;

public final class MismatchingState extends DefaultESDomainError {
  private static final String MESSAGE = "Mismatching command and state: Unable to apply State(%s) to Command(%s)";

  public static MismatchingState of(State state, Command command) {
    return new MismatchingState(state, command);
  }

  private MismatchingState(State state, Command command) {
    var message = MESSAGE.formatted(state.getClass().getName(), command.getClass().getName());
    super(message);
  }
}
