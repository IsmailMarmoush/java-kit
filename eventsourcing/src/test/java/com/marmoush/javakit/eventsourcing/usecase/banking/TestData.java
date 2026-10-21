package com.marmoush.javakit.eventsourcing.usecase.banking;


import com.marmoush.javakit.core.id.Ids;
import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.Saga;
import com.marmoush.javakit.eventsourcing.id.StateId;


import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class TestData {
  // Rule
  public static final AtomicLong counter = new AtomicLong();
  public static final Supplier<Id> idSupplier = () -> Ids.of(counter.getAndIncrement());
  public static final Supplier<Long> timeSupplier = () -> 0L;
  public static final Decider decider = new AccountDecider(idSupplier, timeSupplier);
  public static final Evolver evolver = new AccountEvolver();
  public static final Saga saga = new AccountSaga(idSupplier, timeSupplier);

  // Data
  public static final String ALICE = "alice";
  public static final StateId ALICE_ID = StateId.of(ALICE);

  public static final String BOB = "bob";
  public static final StateId BOB_ID = StateId.of(BOB);

  private TestData() {}
}
