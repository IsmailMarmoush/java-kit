package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountCreated;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.Account;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;

public class AccountEvolver extends Evolver {
  @Override
  public State evolve(Event event) {
    if (event instanceof AccountCreated(EventMeta meta, String name, long balance)) {
      return new OpenAccount(meta, name, balance);
    }
    throw EvolutionException.unhandledEventType(event);
  }

  @Override
  protected State validatedEvolve(State state, Event event) {
    if (state instanceof OpenAccount account && event instanceof Debited debited) {
      return account.withDebit(debited.amount());
    }
    if (state instanceof Account account) {
      return account;
    }
    throw EvolutionException.unhandledStateType(state);
  }
}
