package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;

import java.io.Serializable;
import java.util.Optional;

public interface Command extends SagaSourced, Serializable {
  CommandMeta meta();

  default CommandId commandId() {
    return meta().commandId();
  }

  @Override
  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }
}
