package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.ConfirmDebit;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountEvent;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.CreditRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Credited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;

import java.util.Optional;
import java.util.function.Supplier;

public class AccountSaga extends Saga {
  public AccountSaga(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public Optional<Command> react(Event event) {
    if (!(event instanceof AccountEvent accountEvent)) {
      return Optional.empty();
    }
    return switch (accountEvent) {
      case Debited debited -> Optional.of(new Credit(commandMeta(debited.creditedAcc(), debited.meta().eventId()),
                                                     debited.meta().stateId(),
                                                     debited.amount()));
      case Credited credited ->
              Optional.of(new ConfirmDebit(commandMeta(credited.debitedAcc(), credited.meta().eventId()),
                                           credited.meta().stateId()));
      case CreditRejected rejected ->
              Optional.of(new Credit(commandMeta(rejected.debitedAcc(), rejected.meta().eventId()),
                                     rejected.meta().stateId(),
                                     rejected.amount()));
      default -> Optional.empty();
    };
  }

  private CommandMeta commandMeta(StateId stateId, EventId source) {
    return CommandMeta.builder()
                      .commandId(CommandId.of(idSupplier().get()))
                      .stateId(stateId)
                      .operationType(OperationType.EVOLUTION)
                      .sagaSource(source)
                      .build();
  }
}
