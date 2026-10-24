package com.marmoush.javakit.eventsourcing.aggregate.store;

import com.marmoush.javakit.eventsourcing.Utils;
import com.marmoush.javakit.eventsourcing.aggregate.Aggregate;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class AggregateStoreTest {
  public static final int STATES_SIZE = 100;

  @ParameterizedTest
  @MethodSource("stores")
  void aggregateStore(AggregateStore aggregateStore) {
    // Given
    addAggregates(aggregateStore);

    // When
    stateIdStream().map(aggregateStore::get).map(Assertions::assertThat).forEach(AbstractAssert::isNotNull);
  }

  @Test
  void supportsRemovalClearingIterationAndInjectedMap() {
    //given
    var stateId = StateId.of("state");
    var aggregate = Utils.simpleAggregate(stateId);
    var backingMap = new java.util.HashMap<StateId, Aggregate>();
    var store = AggregateStore.mapStore(backingMap);
    store.computeIfAbsent(stateId, Utils::simpleAggregate);
    store.computeIfAbsent(stateId, ignored -> {
      throw new AssertionError("Existing aggregate should not be replaced");
    });

    //when
    var stored = store.get(stateId);
    var iterated = StreamSupport.stream(store.spliterator(), false).toList();
    store.remove(stateId);

    //then
    assertThat(stored).isNotNull();
    assertThat(iterated).containsExactly(stored);
    assertThat(backingMap).isEmpty();

    //when
    store.computeIfAbsent(stateId, ignored -> aggregate);
    store.clear();

    //then
    assertThat(store.get(stateId)).isNull();
    assertThat(store).isEmpty();
  }

  private static void addAggregates(AggregateStore aggregateStore) {
    stateIdStream().forEach(stateId -> aggregateStore.computeIfAbsent(stateId, Utils::simpleAggregate));
  }

  private static Stream<StateId> stateIdStream() {
    return IntStream.range(0, STATES_SIZE).mapToObj(StateId::of);
  }

  public static Stream<Arguments> stores() {
    return Stream.of(Arguments.of(Named.of("Concurrent map store", AggregateStore.mapStore())));
  }
}
