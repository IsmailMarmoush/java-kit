package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.id.Id;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Saga {
  private final Supplier<Id> IdSupplier;

  public Saga(Supplier<Id> idSupplier) {
    IdSupplier = idSupplier;
  }

  protected abstract Optional<CreationCommand> react(Event event);

  public Supplier<Id> idSupplier() {
    return IdSupplier;
  }
}
