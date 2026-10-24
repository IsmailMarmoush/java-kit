package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.Id;

import java.util.Optional;
import java.util.function.Supplier;

public class SimpleSaga extends Saga {

  public SimpleSaga(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public Optional<Command> react(Event event) {
    return switch (event){
      case StateCreated s -> Optional.empty();
      case StateChanged s-> Optional.of()
      default -> throw new IllegalStateException("Unexpected value: " + event);
    };
  }
}
