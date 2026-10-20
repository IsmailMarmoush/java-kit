package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.command.Command;

public final class UnhandledCommandType extends RuntimeException {
  private static final String MSG = "Unhandled Command: %s[%s] implementation";

  public static UnhandledCommandType of(Command command) {
    return new UnhandledCommandType(command);
  }

  private UnhandledCommandType(Command command) {
    var message = MSG.formatted(command.getClass().getName(), command.meta());
    super(message);
  }
}
