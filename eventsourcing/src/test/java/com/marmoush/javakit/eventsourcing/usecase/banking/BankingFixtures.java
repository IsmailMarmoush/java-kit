package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.state.StateId;

public class BankingFixtures {
  // Rule
  public static final Decider decider = new AccountDecider(EventId::randomUUID);
  public static final Evolver evolver = new AccountEvolver();
  public static final Saga saga = new AccountSaga(CommandId::randomUUID);

  // Data
  public static final String ALICE = "alice";
  public static final StateId ALICE_ID = StateId.of(ALICE);

  public static final String BOB = "bob";
  public static final StateId BOB_ID = StateId.of(BOB);

  private BankingFixtures() {}
}
