package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.core.domain.Result;
import com.marmoush.javakit.eventsourcing.aggregate.store.AggregateStore;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandFailure;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.state.StateId;

import java.util.Optional;

public class Aggregates {
  private final AggregateStore aggregateStore;
  private final AggregateFactory aggregateFactory;

  private Aggregates(AggregateStore aggregateStore, AggregateFactory aggregateFactory) {
    this.aggregateStore = aggregateStore;
    this.aggregateFactory = aggregateFactory;
  }

  public Result<Optional<Event>, CommandFailure> handle(StateId stateId, Command command) {
    aggregateStore.computeIfAbsent(stateId, aggregateFactory::create);
    return aggregateStore.get(stateId).handle(command);
  }

  public static Aggregates create(AggregateStore aggregateStore, AggregateFactory aggregateFactory) {
    return new Aggregates(aggregateStore, aggregateFactory);
  }
}
