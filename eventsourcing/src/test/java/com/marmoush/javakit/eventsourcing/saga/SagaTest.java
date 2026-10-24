package com.marmoush.javakit.eventsourcing.saga;

import com.marmoush.javakit.eventsourcing.Saga;
import com.marmoush.javakit.eventsourcing.event.CreationEventMeta;
import com.marmoush.javakit.eventsourcing.event.EvolutionEventMeta;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.ChangeState;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleSaga;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateChanged;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SagaTest {
  private final Saga saga = new SimpleSaga(CommandId::randomUUID);

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  void react() {
    // Given
    StateId bobId = StateId.of("BOB");
    StateId aliceId = StateId.of("ALICE");

    var creationEventMeta = new CreationEventMeta(EventId.randomUUID(), bobId, CommandId.randomUUID());
    var stateCreated = new StateCreated(creationEventMeta);
    var evolutionEventMeta = new EvolutionEventMeta(EventId.randomUUID(), 1, bobId, CommandId.randomUUID());
    var stateChanged = new StateChanged(evolutionEventMeta);

    // When
    var firstCommand = saga.react(aliceId, stateCreated).get();
    var secondCommand = saga.react(aliceId, stateChanged).get();

    // Then
    assertThat(firstCommand).isInstanceOf(ChangeState.class);
    assertThat(firstCommand.meta().sagaSource()).contains(stateCreated.meta().eventId());
  }

}


