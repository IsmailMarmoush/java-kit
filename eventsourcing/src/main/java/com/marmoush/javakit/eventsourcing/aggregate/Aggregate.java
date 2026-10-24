package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;

public interface Aggregate {
  DecisionResult handle(Command command);

  static Aggregate create(Decider decider, Evolver evolver, EventRepo eventRepo) {
    return new DefaultAggregate(decider, evolver, eventRepo);
  }
}
