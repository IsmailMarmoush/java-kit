package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.EvolutionException;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolutionLogic;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleState;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateChanged;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EvolverTest {
  private final Evolver evolver = new Evolver(new SimpleEvolutionLogic());

  @Test
  void evolveCreation() {
    // Given
    var stateCreated = new StateCreated(eventMeta(0, StateId.of(0)));

    // When
    var event = evolver.evolve(stateCreated);

    // Then
    assertThat(event).isInstanceOf(SimpleState.class);
    assertThat(event.meta().version()).isZero();
  }

  @Test
  void evolveEvolution() {
    // Given
    var stateId = StateId.of(0);
    var someState = new SimpleState(eventMeta(0, stateId));
    var stateChanged = new StateChanged(eventMeta(1, stateId));

    // When
    var event = evolver.evolve(someState, stateChanged);

    // Then
    assertThat(event).isInstanceOf(SimpleState.class);
    assertThat(event.meta().version()).isOne();
  }

  @Test
  void rejectsEvolutionEventsForAnotherState() {
    var state = new SimpleState(eventMeta(0, StateId.of("state")));
    var event = new StateChanged(eventMeta(1, StateId.of("other-state")));

    assertThatThrownBy(() -> evolver.evolve(state, event)).isInstanceOf(EvolutionException.class)
                                                          .hasMessageContaining("Event stateId mismatch");
  }

  private static EventMeta eventMeta(long version, StateId stateId) {
    return EventMeta.builder()
                    .eventId(EventId.of(version))
                    .operationType(version == 0 ? OperationType.CREATION : OperationType.EVOLUTION)
                    .version(version)
                    .stateId(stateId)
                    .commandId(CommandId.of(version))
                    .build();
  }
}
