package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;
import java.util.Optional;

public interface Event extends Partitioned, Versioned, SagaSourced, Serializable {
  EventMeta meta();

  default EventId eventId() {
    return meta().eventId();
  }

  @Override
  default long version() {
    return meta().version();
  }

  default CommandId commandId() {
    return meta().commandId();
  }

  @Override
  default StateId stateId() {
    return meta().stateId();
  }

  @Override
  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }
}
