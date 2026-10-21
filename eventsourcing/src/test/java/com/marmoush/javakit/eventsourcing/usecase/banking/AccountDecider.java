package com.marmoush.javakit.eventsourcing.usecase.banking;


import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.error.MismatchingCommandError;
import com.marmoush.javakit.eventsourcing.exception.UnhandledCommandTypeException;
import com.marmoush.javakit.eventsourcing.exception.UnhandledStateTypeException;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.AccountCommand;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.ChangeName;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.CloseAccount;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.ConfirmDebit;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.CreateAccount;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Debit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountClosed;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountCreated;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.ClosureRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.CreditRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Credited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.DebitConfirmed;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.DebitRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.NameChanged;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.Account;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.ClosedAccount;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;

import java.util.function.Supplier;

public record AccountDecider(Supplier<Id> idSupplier, Supplier<Long> timeSupplier) implements Decider {

  @Override
  public DecisionResult decide(Command command, EventMeta eventMeta) {
    if (command instanceof AccountCommand accountCommand) {
      return decide(accountCommand, eventMeta);
    } else {
      throw UnhandledCommandTypeException.of(command);
    }
  }

  @Override
  public DecisionResult decide(State state, Command command, EventMeta eventMeta) {
    if (state instanceof Account account) {
      if (command instanceof AccountCommand accountCommand) {
        return decide(account, accountCommand, eventMeta);
      } else {
        throw UnhandledCommandTypeException.of(command);
      }
    } else {
      throw UnhandledStateTypeException.of(state);
    }
  }

  @SuppressWarnings("SwitchStatementWithTooFewBranches")
  private DecisionResult decide(AccountCommand command, EventMeta newEventMeta) {
    return switch (command) {
      case CreateAccount cmd -> DecisionEvent.of(new AccountCreated(newEventMeta, cmd.accountName(), cmd.balance()));
      default -> throw UnhandledCommandTypeException.of(command);
    };
  }

  private DecisionResult decide(Account state, AccountCommand command, EventMeta newEventMeta) {
    return switch (state) {
      case OpenAccount openAccount -> decide(openAccount, command, newEventMeta);
      case ClosedAccount acc -> decide(acc, command, newEventMeta);
    };
  }

  private DecisionResult decide(OpenAccount account, AccountCommand command, EventMeta meta) {
    return switch (command) {
      case CreateAccount cmd -> DecisionFailure.of(MismatchingCommandError.of(account, cmd));
      case ChangeName cmd -> DecisionEvent.of(new NameChanged(meta, cmd.name()));
      case Debit cmd -> tryToDebit(cmd, account, meta);
      case Credit cmd -> DecisionEvent.of(new Credited(meta, cmd.debitedAcc(), cmd.amount()));
      case ConfirmDebit _ -> DecisionEvent.of(new DebitConfirmed(meta));
      case CloseAccount _ -> tryToClose(account, meta);
    };
  }

  private DecisionResult decide(ClosedAccount state, AccountCommand command, EventMeta meta) {
    return switch (command) {
      case Credit cmd -> DecisionEvent.of(new CreditRejected(meta, cmd.debitedAcc(), cmd.amount()));
      case ConfirmDebit _ -> DecisionEvent.of(new DebitConfirmed(meta));
      case ChangeName cmd -> DecisionFailure.of(MismatchingCommandError.of(state, cmd));
      case Debit cmd -> DecisionFailure.of(MismatchingCommandError.of(state, cmd));
      case CreateAccount cmd -> DecisionFailure.of(MismatchingCommandError.of(state, cmd));
      case CloseAccount cmd -> DecisionFailure.of(MismatchingCommandError.of(state, cmd));
    };
  }

  private static DecisionResult tryToDebit(Debit cmd, OpenAccount account, EventMeta meta) {
    if (account.canDebit(cmd.amount())) {
      return DecisionEvent.of(new Debited(meta, cmd.creditedAcc(), cmd.amount()));
    } else {
      return DecisionEvent.of(new DebitRejected(meta));
    }
  }

  private static DecisionResult tryToClose(OpenAccount openAccount, EventMeta meta) {
    if (openAccount.hasOngoingDebit()) {
      return DecisionEvent.of(new ClosureRejected(meta));
    } else {
      return DecisionEvent.of(new AccountClosed(meta));
    }
  }
}
