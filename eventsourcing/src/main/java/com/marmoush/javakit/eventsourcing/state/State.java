package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serializable;

public interface State extends Partitioned, Serializable {
  StateMeta meta();

  State withMeta(StateMeta meta);

  @Override
  default StateId stateId() {
    return meta().stateId();
  }
}
