package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.eventsourcing.aggregate.store.AggregateStore;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.CreateState;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AggregatesTest {
  private final Aggregates aggregates = Aggregates.create(AggregateStore.mapStore(), new DomainAggregateFactory());
  private final StateId stateId = StateId.of(0);

  @ParameterizedTest
  @MethodSource("stores")
  void defaultSystem(AggregateStore aggregateStore) {
    // Given
    var meta = new CommandMeta(CommandId.of(UUID.randomUUID()), stateId);
    // When
    var event = aggregates.handle(stateId, new CreateState(meta)).get();
    // Then
    assertThat(event).isInstanceOf(StateCreated.class);
  }

  public static Stream<Arguments> stores() {
    return Stream.of(Arguments.of(Named.of("Concurrent map store", AggregateStore.mapStore())));
  }
}
