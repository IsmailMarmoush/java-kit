package com.marmoush.javakit.eventsourcing.saga;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.processor.Saga;
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
  void react() {
    // Given
    StateId bobId = StateId.of("BOB");
    var createdMeta = EventMeta.builder()
                               .eventId(EventId.randomUUID())
                               .operationType(OperationType.CREATION)
                               .stateId(bobId)
                               .commandId(CommandId.randomUUID())
                               .build();
    var stateCreated = new StateCreated(createdMeta);
    var changedMeta = EventMeta.builder()
                               .eventId(EventId.randomUUID())
                               .operationType(OperationType.EVOLUTION)
                               .version(1)
                               .stateId(bobId)
                               .commandId(CommandId.randomUUID())
                               .build();
    var stateChanged = new StateChanged(changedMeta);

    // When
    var createdCommand = saga.react(stateCreated);
    var changedCommand = saga.react(stateChanged);

    // Then
    assertThat(createdCommand).isEmpty();
    assertThat(changedCommand).hasValueSatisfying(command -> {
      assertThat(command).isInstanceOf(ChangeState.class);
      assertThat(command.meta().sagaSource()).contains(stateChanged.meta().eventId());
    });
  }

}

