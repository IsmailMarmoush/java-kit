package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.processor.DecisionLogic;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.AccountCommand;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.ChangeName;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.CloseAccount;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.CreateAccount;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Debit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountClosed;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountCreated;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountEvent;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Credited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.DebitRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.NameChanged;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;

public class AccountDecisionLogic implements DecisionLogic {

  @Override
  public DecisionResult decide(Command command, EventMeta meta) {
    if (command instanceof CreateAccount cmd) {
      return DecisionResult.success(new AccountCreated(meta, cmd.accountName(), cmd.balance()));
    }
    throw DecisionException.unhandledCommandType(command);
  }

  @Override
  public DecisionResult decide(State state, Command command, EventMeta meta) {
    if (state instanceof OpenAccount account && command instanceof AccountCommand accountCommand) {
      return DecisionResult.success(decide(account, accountCommand, meta));
    }
    throw DecisionException.invalidCommand(command, state);
  }

  AccountEvent decide(OpenAccount account, AccountCommand command, EventMeta meta) {
    return switch (command) {
      case ChangeName cmd -> new NameChanged(meta, cmd.name());
      case Credit cmd -> new Credited(meta, cmd.debitedAcc(), account.balance());
      case Debit cmd -> account.canDebit(cmd.amount()) ? new Debited(meta, cmd.creditedAcc(), cmd.amount())
                                                       : new DebitRejected(meta);
      case CloseAccount cmd -> new AccountClosed(meta);
      case CreateAccount cmd -> throw DecisionException.invalidCommand(cmd, account);
    };
  }
}
