package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.id.StateId;

public interface AggregateFactory {
  Aggregate create(StateId stateId);
}
