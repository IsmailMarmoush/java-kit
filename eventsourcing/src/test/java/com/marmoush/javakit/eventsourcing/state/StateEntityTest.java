package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class StateEntityTest {
  @Test
  void builderCreatesEntityWithEventMetadataAndState() {
    var meta = EventMeta.builder()
                        .eventId(EventId.of("event"))
                        .operationType(OperationType.CREATION)
                        .stateId(StateId.of("state"))
                        .commandId(CommandId.of("command"))
                        .build();

    var entity = StateEntity.<String>builder().meta(meta).state("payload").build();

    assertThat(entity.meta()).isSameAs(meta);
    assertThat(entity.state()).isEqualTo("payload");
    assertThat(entity).isEqualTo(StateEntity.<String>builder().meta(meta).state("payload").build());
  }

  @Test
  void requiresMetadata() {
    assertThatNullPointerException().isThrownBy(() -> new StateEntity<>(null, "payload"));
  }
}
