package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;

import java.util.function.Supplier;

public class TestData {
  // Rule
  public static final Supplier<Long> timeSupplier = () -> 0L;
  public static final Decider decider = new AccountDecider(EventId::randomUUID, timeSupplier);
  public static final Evolver evolver = new AccountEvolver();
  public static final Saga saga = new AccountSaga(CommandId::randomUUID, timeSupplier);

  // Data
  public static final String ALICE = "alice";
  public static final StateId ALICE_ID = StateId.of(ALICE);

  public static final String BOB = "bob";
  public static final StateId BOB_ID = StateId.of(BOB);

  private TestData() {}
}
