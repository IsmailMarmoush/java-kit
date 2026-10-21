package com.marmoush.javakit.eventsourcing.error;

import com.marmoush.javakit.eventsourcing.command.Command;

public final class DuplicateCommand extends DefaultESDomainError {
  private static final String MESSAGE = "Duplicate Command(commandMeta=%s)";

  public static DuplicateCommand of(Command command) {
    return new DuplicateCommand(command);
  }

  private DuplicateCommand(Command command) {
    var message = MESSAGE.formatted(command.meta());
    super(message);
  }
}
