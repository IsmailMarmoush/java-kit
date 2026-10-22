package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.id.PartitionId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;

public record StateMeta(StateId stateId, long version) implements Partitioned, Versioned, Serializable {
  public StateMeta {
    if (version < 0) {
      throw new IllegalArgumentException("Version can't be less than zero!");
    }
  }

  public StateMeta(StateId stateId) {
    this(stateId, 0);
  }

  @Override
  public PartitionId pKey() {
    return PartitionId.of(stateId);
  }

  public StateMeta incrementVersion() {
    return new StateMeta(stateId, version + 1);
  }
}
