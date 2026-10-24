package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;

import java.util.Optional;

public interface CreationCommand extends Command {
  @Override
  CreationCommandMeta meta();

  default CommandId commandId() {
    return meta().commandId();
  }

  @Override
  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }
}
