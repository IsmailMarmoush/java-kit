package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.repo.CommandPublisher;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.processor.Saga;

import java.util.Optional;

public class SagaHandler {
  private final Saga saga;
  private final CommandPublisher commandPublisher;

  public SagaHandler(Saga saga, CommandPublisher commandPublisher) {
    this.saga = saga;
    this.commandPublisher = commandPublisher;
  }

  public Optional<Command> react(Event event) {
    Optional<Command> result = saga.react(event);
    result.ifPresent(commandPublisher::publish);
    return result;
  }
}
