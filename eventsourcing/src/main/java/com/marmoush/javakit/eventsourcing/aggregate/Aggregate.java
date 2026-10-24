package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;

public interface Aggregate {
  DecisionResult handle(Command command);

  static Aggregate create(Decider decider, Evolver evolver, EventRepo eventRepo) {
    return new DefaultAggregate(decider, evolver, eventRepo);
  }
}
