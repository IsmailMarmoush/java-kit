package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.aggregate.Aggregate;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.id.Id;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;

public class Utils {
  private Utils() {}

  public static Decider simpleDecider() {
    return new SimpleDecider(() -> Id.of(0), () -> 0L);
  }

  public static Aggregate simpleAggregate(StateId stateId) {
    return Aggregate.create(stateId, Utils.simpleDecider(), new SimpleEvolver(), EventRepo.inMemory());
  }
}
