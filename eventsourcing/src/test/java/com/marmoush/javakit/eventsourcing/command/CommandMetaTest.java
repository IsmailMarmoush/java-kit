package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandMetaTest {
  private final StateId stateId = StateId.of(0);

  @Test
  void exposesRequiredMetadataAndOptionalSagaSource() {
    var commandId = CommandId.of(12);
    var sagaSource = EventId.of(34);
    var meta = CommandMeta.builder()
                          .commandId(commandId)
                          .stateId(stateId)
                          .operationType(OperationType.EVOLUTION)
                          .sagaSource(sagaSource)
                          .build();
    var same = CommandMeta.builder()
                          .commandId(commandId)
                          .stateId(stateId)
                          .operationType(OperationType.EVOLUTION)
                          .sagaSource(sagaSource)
                          .build();

    Assertions.assertThat(meta.commandId()).isEqualTo(commandId);
    Assertions.assertThat(meta.stateId()).isEqualTo(stateId);
    Assertions.assertThat(meta.operationType()).isEqualTo(OperationType.EVOLUTION);
    Assertions.assertThat(meta.sagaSource()).contains(sagaSource);
    Assertions.assertThat(meta).isEqualTo(same).hasSameHashCodeAs(same);
    Assertions.assertThat(CommandMeta.builder()
                                     .commandId(commandId)
                                     .stateId(stateId)
                                     .operationType(OperationType.CREATION)
                                     .build()
                                     .sagaSource()).isEmpty();
    Assertions.assertThatNullPointerException()
              .isThrownBy(() -> CommandMeta.builder().stateId(stateId).operationType(OperationType.CREATION).build());
  }
}
