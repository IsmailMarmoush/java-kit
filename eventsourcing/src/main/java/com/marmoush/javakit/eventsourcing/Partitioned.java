package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.state.StateId;

public interface Partitioned {

  /**
   * @return the partitioning Key
   */
  StateId pKey();

  default boolean isInPartition(int partition, int totalPartitions) {
    return partition == partition(totalPartitions);
  }

  default int partition(int totalPartitions) {
    return partition(pKey(), totalPartitions);
  }

  static int partition(StateId pKey, int totalPartitions) {
    var hash = (pKey.hashCode() == Integer.MIN_VALUE) ? Integer.MAX_VALUE : pKey.hashCode();
    return Math.abs(hash) % totalPartitions;
  }
}
