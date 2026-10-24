package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.processor.Saga;

import java.util.Optional;
import java.util.function.Supplier;

public class SimpleSaga extends Saga {

  public SimpleSaga(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public Optional<Command> react(Event event) {
    return switch (event) {
      case StateCreated s -> Optional.empty();
      case StateChanged s -> Optional.of(new ChangeState(CommandMeta.builder()
                                                                    .commandId(CommandId.of(idSupplier().get()))
                                                                    .stateId(s.meta().stateId())
                                                                    .operationType(OperationType.EVOLUTION)
                                                                    .sagaSource(s.meta().eventId())
                                                                    .build()));
      default -> throw new IllegalStateException("Unexpected value: " + event);
    };
  }
}
