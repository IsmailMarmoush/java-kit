package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.Decider;
import com.marmoush.javakit.eventsourcing.Evolver;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;

import java.util.UUID;

public class DomainAggregateFactory implements AggregateFactory {
  private final Decider decider = new SimpleDecider(UUID::randomUUID, () -> 0L);
  private final Evolver evolver = new SimpleEvolver();
  private final EventRepo eventRepo = EventRepo.inMemory();

  @Override
  public Aggregate create(StateId stateId) {
    return Aggregate.create(stateId, decider, evolver, eventRepo);
  }
}
