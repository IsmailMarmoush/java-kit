package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.PartitionId;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.Versioned;


import java.io.Serializable;

public interface State extends Partitioned, Versioned, Serializable {
  StateMeta meta();

  State withMeta(StateMeta meta);

  default StateId stateId() {
    return meta().stateId();
  }

  default @Override PartitionId pKey() {
    return meta().pKey();
  }

  default @Override long version() {
    return meta().version();
  }
}
