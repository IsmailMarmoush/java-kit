package com.marmoush.javakit.eventsourcing.aggregate.error;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.state.StateId;

public final class MismatchingCommandStateId extends DefaultESDomainError {
  private static final String MESSAGE = "Mismatching command and state Ids: State(StateId=%s) != Command(StateId=%s)";

  public static MismatchingCommandStateId of(StateId stateId, Command command) {
    return new MismatchingCommandStateId(stateId, command);
  }

  private MismatchingCommandStateId(StateId stateId, Command command) {
    var message = MESSAGE.formatted(stateId.value(), command.stateId().value());
    super(message);
  }
}
