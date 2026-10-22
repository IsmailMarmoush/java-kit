package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.Versioned;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.PartitionId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;
import java.util.Optional;

public interface Event extends Partitioned, Versioned, Serializable {
  EventMeta meta();

  default EventId eventId() {
    return meta().eventId();
  }

  default CommandId commandId() {
    return meta().commandId();
  }

  default StateId stateId() {
    return meta().stateId();
  }

  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }

  default @Override PartitionId pKey() {
    return meta().pKey();
  }

  default @Override long version() {
    return meta().version();
  }
}
