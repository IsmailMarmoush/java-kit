package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.id.StateId;

public interface Aggregate {
  StateId stateId();

  DecisionResult handle(Command command);

  static Aggregate create(StateId stateId, Decider decider, Evolver evolver, EventRepo eventRepo) {
    return new SyncAggregate(stateId, decider, evolver, eventRepo);
  }
}
