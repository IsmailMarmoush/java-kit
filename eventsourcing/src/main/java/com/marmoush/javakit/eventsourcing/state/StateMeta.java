package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.Versioned;

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
  public Id pKey() {
    return stateId;
  }

  public StateMeta incrementVersion() {
    return new StateMeta(stateId, version + 1);
  }
}
