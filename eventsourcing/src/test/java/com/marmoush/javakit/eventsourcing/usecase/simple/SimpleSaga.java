package com.marmoush.javakit.eventsourcing.usecase.simple;


import com.marmoush.javakit.eventsourcing.Saga;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.Event;


import java.util.Optional;
import java.util.function.Supplier;

public record SimpleSaga(Supplier<Id> idSupplier, Supplier<Long> timeSupplier) implements Saga {
  @Override
  public Optional<Command> react(Event event) {
    return Optional.of(new ChangeState(commandMeta(StateId.of(0), event)));
  }
}
