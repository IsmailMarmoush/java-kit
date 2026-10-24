package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Debit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.DebitRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;

import java.util.function.Supplier;

public class AccountDecider extends Decider {
  public AccountDecider(Supplier<Id> idSupplier) {
    super(idSupplier);
  }

  @Override
  public DecisionResult decide(Command command) {
    throw DecisionException.unhandledCommandType(command);
  }

  @Override
  protected DecisionResult validatedDecide(State state, Command command) {
    if (state instanceof OpenAccount account && command instanceof Debit debit) {
      var meta = eventMeta(state, command);
      return DecisionResult.success(
              account.canDebit(debit.amount()) ? new Debited(meta, debit.creditedAcc(), debit.amount())
                                               : new DebitRejected(meta));
    }
    throw DecisionException.invalidCommand(command, state);
  }
}
