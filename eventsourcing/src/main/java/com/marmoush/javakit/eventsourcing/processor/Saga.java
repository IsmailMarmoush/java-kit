package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.Id;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Saga {
  private final Supplier<Id> IdSupplier;

  public Saga(Supplier<Id> idSupplier) {
    IdSupplier = idSupplier;
  }

  public abstract Optional<Command> react(Event event);

  protected Supplier<Id> idSupplier() {
    return IdSupplier;
  }
}
