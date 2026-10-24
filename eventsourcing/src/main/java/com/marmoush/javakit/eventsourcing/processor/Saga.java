package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.Event;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Saga {
  private final Supplier<Id> idSupplier;

  protected Saga(Supplier<Id> idSupplier) {
    this.idSupplier = idSupplier;
  }

  public abstract Optional<Command> react(Event event);

  protected Supplier<Id> idSupplier() {
    return idSupplier;
  }
}
