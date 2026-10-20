package com.marmoush.javakit.eventsourcing.aggregate.exception;

import com.marmoush.javakit.eventsourcing.command.Command;

public final class UnhandledCommandTypeException extends RuntimeException {
  private static final String MSG = "Unhandled Command: %s[%s] implementation";

  public static UnhandledCommandTypeException of(Command command) {
    return new UnhandledCommandTypeException(command);
  }

  private UnhandledCommandTypeException(Command command) {
    var message = MSG.formatted(command.getClass().getName(), command.meta());
    super(message);
  }
}
