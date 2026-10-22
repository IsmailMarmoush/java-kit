package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.StateId;

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
}
