package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandMetaTest {
  private final StateId stateId = StateId.of(0);

  @Test
  void init() {
    // Given
    var commandMeta = new CommandMeta(CommandId.of(0), stateId, 0);

    // Then
    Assertions.assertThat(commandMeta.pKey().value()).isEqualTo(stateId.value());
  }

  @Test
  void equality() {
    // Given
    var commandMeta1 = new CommandMeta(CommandId.of(0), stateId, 0);
    var commandMeta2 = new CommandMeta(CommandId.of(0), stateId, 0);

    // Then
    Assertions.assertThat(commandMeta1).isEqualTo(commandMeta2);
  }

  @Test
  void nullSagaSource() {
    // Given
    var meta = new CommandMeta(CommandId.of(0), stateId, 0, null);

    // Then
    Assertions.assertThat(meta.sagaSource()).isEmpty();
  }

  @Test
  void exposesAllMetadataAndSagaSource() {
    //given
    var commandId = CommandId.of(12);
    var sagaSource = EventId.of(34);
    var meta = new CommandMeta(commandId, stateId, 56L, sagaSource);

    //when
    var partitionKey = meta.pKey();

    //then
    Assertions.assertThat(meta.commandId()).isEqualTo(commandId);
    Assertions.assertThat(meta.stateId()).isEqualTo(stateId);
    Assertions.assertThat(meta.timestamp()).isEqualTo(56L);
    Assertions.assertThat(meta.sagaSource()).contains(sagaSource);
    Assertions.assertThat(partitionKey.value()).isEqualTo(stateId.value());
    Assertions.assertThat(meta.toString()).contains("commandId=12", "stateId=0", "timestamp=56", "sagaSource=34");
  }

  @Test
  void equalityIncludesAllFieldsAndHandlesOtherTypes() {
    //given
    var meta = new CommandMeta(CommandId.of(1), stateId, 10L, EventId.of(2));
    var same = new CommandMeta(CommandId.of(1), stateId, 10L, EventId.of(2));

    //when
    var sameValue = meta.equals(same);

    //then
    Assertions.assertThat(sameValue).isTrue();
    Assertions.assertThat(meta)
              .hasSameHashCodeAs(same)
              .isNotEqualTo(new CommandMeta(CommandId.of(9), stateId, 10L, EventId.of(2)))
              .isNotEqualTo(new CommandMeta(CommandId.of(1), stateId, 11L, EventId.of(2)))
              .isNotEqualTo(new CommandMeta(CommandId.of(1), StateId.of(1), 10L, EventId.of(2)))
              .isNotEqualTo(new CommandMeta(CommandId.of(1), stateId, 10L, EventId.of(3)));
  }

  @Test
  void constructorsDefaultTimestampAndSagaSourceAppropriately() {
    //given
    var before = System.currentTimeMillis();

    //when
    var currentTimestampMeta = new CommandMeta(CommandId.of(1), stateId);
    var timestampMeta = new CommandMeta(CommandId.of(2), stateId, 99L);

    //then
    Assertions.assertThat(currentTimestampMeta.timestamp()).isBetween(before, System.currentTimeMillis());
    Assertions.assertThat(currentTimestampMeta.sagaSource()).isEmpty();
    Assertions.assertThat(timestampMeta.timestamp()).isEqualTo(99L);
    Assertions.assertThat(timestampMeta.sagaSource()).isEmpty();
    Assertions.assertThatNullPointerException().isThrownBy(() -> new CommandMeta(null, stateId));
    Assertions.assertThatNullPointerException().isThrownBy(() -> new CommandMeta(CommandId.of(1), null));
  }
}
