package com.marmoush.javakit.eventsourcing.error;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.id.StateId;

public final class MismatchingCommandStateIdError extends DefaultESDomainError {
  private static final String MESSAGE = "Mismatching command and state Ids: State(StateId=%s) != Command(StateId=%s)";

  public static MismatchingCommandStateIdError of(StateId stateId, Command command) {
    return new MismatchingCommandStateIdError(stateId, command);
  }

  private MismatchingCommandStateIdError(StateId stateId, Command command) {
    var message = MESSAGE.formatted(stateId.value(), command.stateId().value());
    super(message);
  }
}
