package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.Command;

import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.util.Optional;
import java.util.function.Supplier;

public interface Saga {
  Supplier<CommandId> commandIdSupplier();

  Supplier<Long> timeSupplier();

  Optional<Command> react(Event event);

  default CommandMeta commandMeta(StateId stateId, Event event) {
    return new CommandMeta(commandIdSupplier().get(), stateId, timeSupplier().get(), event.meta().eventId());
  }
}
