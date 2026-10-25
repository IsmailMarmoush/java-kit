package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CommandEntityTest {
  @Test
  void builderCreatesEntityWithCommandMetadataAndState() {
    var meta = CommandMeta.builder()
                          .commandId(CommandId.of("command"))
                          .stateId(StateId.of("state"))
                          .operationType(OperationType.CREATION)
                          .build();

    var entity = CommandEntity.<String>builder().meta(meta).state("payload").build();

    assertThat(entity.meta()).isSameAs(meta);
    assertThat(entity.state()).isEqualTo("payload");
    assertThat(entity).isEqualTo(CommandEntity.<String>builder().meta(meta).state("payload").build());
  }

  @Test
  void requiresMetadata() {
    assertThatNullPointerException().isThrownBy(() -> new CommandEntity<>(null, "payload"));
  }
}
