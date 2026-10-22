package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.Partitioned;

import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.PartitionId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;
import java.util.Optional;

public interface Command extends Partitioned, Serializable {
  CommandMeta meta();

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
}
