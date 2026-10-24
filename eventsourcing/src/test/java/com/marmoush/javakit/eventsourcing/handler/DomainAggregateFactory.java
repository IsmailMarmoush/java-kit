package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;

public class DomainAggregateFactory implements AggregateFactory {
  private final Decider decider = new SimpleDecider(EventId::randomUUID, () -> 0L);
  private final Evolver evolver = new SimpleEvolver();
  private final EventRepo eventRepo = EventRepo.inMemory();

  @Override
  public CommandHandler create(StateId stateId) {
    return CommandHandler.create(stateId, decider, evolver, eventRepo);
  }
}
