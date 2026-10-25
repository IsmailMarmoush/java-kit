package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class EventEntityTest {
  @Test
  void builderCreatesEntityWithEventMetadataAndState() {
    var meta = EventMeta.builder()
                        .eventId(EventId.of("event"))
                        .operationType(OperationType.CREATION)
                        .stateId(StateId.of("state"))
                        .commandId(CommandId.of("command"))
                        .build();

    var entity = EventEntity.<String>builder().meta(meta).state("payload").build();

    assertThat(entity.meta()).isSameAs(meta);
    assertThat(entity.state()).isEqualTo("payload");
    assertThat(entity).isEqualTo(EventEntity.<String>builder().meta(meta).state("payload").build());
  }

  @Test
  void requiresMetadata() {
    assertThatNullPointerException().isThrownBy(() -> new EventEntity<>(null, "payload"));
  }
}
