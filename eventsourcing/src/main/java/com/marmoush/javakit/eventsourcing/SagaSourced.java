package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.id.EventId;

import java.util.Optional;

public interface SagaSourced {
  Optional<EventId> sagaSource();
}
