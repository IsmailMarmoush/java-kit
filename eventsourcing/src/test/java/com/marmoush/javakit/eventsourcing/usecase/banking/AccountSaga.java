package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.Saga;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.CreationCommandMeta;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommandMeta;
import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.ConfirmDebit;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountEvent;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.CreditRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Credited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;

import java.util.Optional;
import java.util.function.Supplier;

public record AccountSaga(Supplier<Id> IdSupplier) implements Saga {

  @Override
  public Optional<Command> react(Event event) {
    if (event instanceof AccountEvent accountEvent) {
      return apply(accountEvent);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Command> apply(AccountEvent event) {
    return switch (event) {
      case Debited debited -> Optional.of(new Credit(evolutionCommandMeta(debited.creditedAcc(), debited),
                                                     debited.accountId(),
                                                     debited.amount()));
      case Credited credited ->
              Optional.of(new ConfirmDebit(evolutionCommandMeta(credited.debitedAcc(), credited), credited.accountId()));
      case CreditRejected rejected -> Optional.of(new Credit(evolutionCommandMeta(rejected.debitedAcc(), rejected),
                                                             rejected.accountId(),
                                                             rejected.amount()));
      default -> Optional.empty();
    };
  }

  @Override
  protected Optional<CreationCommand> react(CreationEvent event, CreationCommandMeta creationCommandMeta) {
    return Optional.empty();
  }

  @Override
  protected Optional<CreationCommand> react(EvolutionEvent event, CreationCommandMeta creationCommandMeta) {
    return Optional.empty();
  }

  @Override
  protected Optional<EvolutionCommand> react(CreationEvent event, EvolutionCommandMeta evolutionCommandMeta) {
    return Optional.empty();
  }

  @Override
  protected Optional<EvolutionCommand> react(EvolutionEvent event, EvolutionCommandMeta evolutionCommandMeta) {
    return Optional.empty();
  }
}
