package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.repo.CommandPublisher;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.processor.Saga;

import java.util.Optional;

public interface SagaHandler {
  Optional<Command> react(Event event);

  static SagaHandler create(Saga saga, CommandPublisher publisher) {
    return new DefaultSagaHandler(saga, publisher);
  }
}
