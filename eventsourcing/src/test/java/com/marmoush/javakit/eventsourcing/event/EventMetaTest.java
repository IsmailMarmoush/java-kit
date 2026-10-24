package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventMetaTest {
  private final StateId stateId = StateId.of(0);

  @Test
  void init() {
    // When
    var eventMeta = new EventMeta(EventId.of(0), 0, stateId, CommandId.of(0), 0);

    // Then
    Assertions.assertThat(eventMeta.pKey().value()).isEqualTo(stateId.value());
  }

  @Test
  void wrongVersion() {
    Assertions.assertThatIllegalArgumentException()
              .isThrownBy(() -> new EventMeta(EventId.of(0), -1, stateId, CommandId.of(0), 0));
  }

  @Test
  void equality() {
    // Given
    var eventMeta1 = new EventMeta(EventId.of(0), 0, stateId, CommandId.of(0), 0);
    var eventMeta2 = new EventMeta(EventId.of(0), 0, stateId, CommandId.of(0), 0);

    // Then
    Assertions.assertThat(eventMeta1).isEqualTo(eventMeta2);
  }

  @Test
  void nullSagaSource() {
    // Given
    var meta = new EventMeta(EventId.of(0), 0, stateId, CommandId.of(0), 0, null);

    // Then
    Assertions.assertThat(meta.sagaSource()).isEmpty();
  }

  @Test
  void exposesAllMetadataAndSagaSource() {
    //given
    var eventId = EventId.of(12);
    var commandId = CommandId.of(34);
    var sagaSource = EventId.of(56);
    var meta = new EventMeta(eventId, 7, stateId, commandId, 89L, sagaSource);

    //when
    var partitionKey = meta.pKey();

    //then
    Assertions.assertThat(meta.eventId()).isEqualTo(eventId);
    Assertions.assertThat(meta.version()).isEqualTo(7);
    Assertions.assertThat(meta.stateId()).isEqualTo(stateId);
    Assertions.assertThat(meta.commandId()).isEqualTo(commandId);
    Assertions.assertThat(meta.createdAt()).isEqualTo(89L);
    Assertions.assertThat(meta.sagaSource()).contains(sagaSource);
    Assertions.assertThat(partitionKey.value()).isEqualTo(stateId.value());
    Assertions.assertThat(meta.toString())
              .contains("eventId=12", "version=7", "stateId=0", "commandId=34", "timestamp=89", "sagaSource=56");
  }

  @Test
  void equalityIncludesAllFieldsAndHandlesOtherTypes() {
    //given
    var meta = new EventMeta(EventId.of(1), 2, stateId, CommandId.of(3), 4L, EventId.of(5));
    var same = new EventMeta(EventId.of(1), 2, stateId, CommandId.of(3), 4L, EventId.of(5));

    //when
    var sameValue = meta.equals(same);

    //then
    Assertions.assertThat(sameValue).isTrue();
    Assertions.assertThat(meta)
              .hasSameHashCodeAs(same)
              .isNotEqualTo(new EventMeta(EventId.of(9), 2, stateId, CommandId.of(3), 4L, EventId.of(5)))
              .isNotEqualTo(new EventMeta(EventId.of(1), 8, stateId, CommandId.of(3), 4L, EventId.of(5)))
              .isNotEqualTo(new EventMeta(EventId.of(1), 2, StateId.of(1), CommandId.of(3), 4L, EventId.of(5)))
              .isNotEqualTo(new EventMeta(EventId.of(1), 2, stateId, CommandId.of(9), 4L, EventId.of(5)))
              .isNotEqualTo(new EventMeta(EventId.of(1), 2, stateId, CommandId.of(3), 9L, EventId.of(5)))
              .isNotEqualTo(new EventMeta(EventId.of(1), 2, stateId, CommandId.of(3), 4L, EventId.of(9)));
  }

  @Test
  void constructorsUseCurrentTimestampAndValidateRequiredValues() {
    //given
    var before = System.currentTimeMillis();

    //when
    var defaultTimestampMeta = new EventMeta(EventId.of(1), 0, stateId, CommandId.of(1));
    var timestampMeta = new EventMeta(EventId.of(2), 0, stateId, CommandId.of(2), 99L);

    //then
    Assertions.assertThat(defaultTimestampMeta.createdAt()).isBetween(before, System.currentTimeMillis());
    Assertions.assertThat(defaultTimestampMeta.sagaSource()).isEmpty();
    Assertions.assertThat(timestampMeta.createdAt()).isEqualTo(99L);
    Assertions.assertThat(timestampMeta.sagaSource()).isEmpty();
    Assertions.assertThatNullPointerException().isThrownBy(() -> new EventMeta(null, 0, stateId, CommandId.of(1)));
    Assertions.assertThatNullPointerException()
              .isThrownBy(() -> new EventMeta(EventId.of(1), 0, null, CommandId.of(1)));
    Assertions.assertThatNullPointerException().isThrownBy(() -> new EventMeta(EventId.of(1), 0, stateId, null));
  }
}
