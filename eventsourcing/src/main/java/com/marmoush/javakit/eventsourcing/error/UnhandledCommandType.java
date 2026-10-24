package com.marmoush.javakit.eventsourcing.error;

import com.marmoush.javakit.eventsourcing.command.Command;

public final class UnhandledCommandType extends DefaultESDomainError {
  private static final String MESSAGE = "Unhandled Command type=%s";

  public static UnhandledCommandType of(Command command) {
    return new UnhandledCommandType(command);
  }

  private UnhandledCommandType(Command command) {
    var message = MESSAGE.formatted(command.getClass().getName());
    super(message);
  }
}
