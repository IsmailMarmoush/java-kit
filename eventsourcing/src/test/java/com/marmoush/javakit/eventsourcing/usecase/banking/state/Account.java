package com.marmoush.javakit.eventsourcing.usecase.banking.state;

import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.state.State;

public sealed interface Account extends State permits OpenAccount, ClosedAccount {
  default StateId accountId() {
    return meta().stateId();
  }
}
