package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.handler.CommandHandler;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;

public class Utils {
  private Utils() {}

  public static Decider simpleDecider() {
    return new SimpleDecider(EventId::randomUUID, () -> 0L);
  }

  public static CommandHandler simpleAggregate(StateId stateId) {
    return CommandHandler.create(stateId, Utils.simpleDecider(), new SimpleEvolver(), EventRepo.inMemory());
  }
}
