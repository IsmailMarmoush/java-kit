package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.event.EventId;

import java.util.Optional;

public interface SagaSourced {
  Optional<EventId> sagaSource();
}
