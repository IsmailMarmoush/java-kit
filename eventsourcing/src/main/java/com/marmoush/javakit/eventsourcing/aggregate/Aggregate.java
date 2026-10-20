package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.state.StateId;

public interface Aggregate {
  StateId stateId();

  DecisionResult handle(Command command);

  static Aggregate create(StateId stateId, Decider decider, Evolver evolver, EventRepo eventRepo) {
    return new SyncAggregate(stateId, decider, evolver, eventRepo);
  }
}
