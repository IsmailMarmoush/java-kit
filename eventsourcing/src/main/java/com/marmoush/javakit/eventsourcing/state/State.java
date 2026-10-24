package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;
import java.util.Optional;

public interface State extends Partitioned, Versioned, SagaSourced, Serializable {
  EventMeta meta();

  @Override
  default StateId stateId() {
    return meta().stateId();
  }

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
  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }
}
