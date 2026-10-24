package com.marmoush.javakit.eventsourcing.saga;

import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;
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

    var eventMeta = new EventMeta(EventId.randomUUID(), bobId, CommandId.randomUUID());
    var stateCreated = new StateCreated(eventMeta);
    var eventMeta = new EventMeta(EventId.randomUUID(), 1, bobId, CommandId.randomUUID());
    var stateChanged = new StateChanged(eventMeta);

    // When
    var firstCommand = saga.react(aliceId, stateCreated).get();
    var secondCommand = saga.react(aliceId, stateChanged).get();

    // Then
    assertThat(firstCommand).isInstanceOf(ChangeState.class);
    assertThat(firstCommand.meta().sagaSource()).contains(stateCreated.meta().eventId());
  }

}


