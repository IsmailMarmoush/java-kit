package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.event.EventId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class StateIdTest {
  @Test
  void testOfString() {
    StateId id = StateId.of("123");
    Assertions.assertThat(id.value()).isEqualTo("123");
  }

  @Test
  void testOfLong() {
    StateId id = StateId.of(123L);
    Assertions.assertThat(id.value()).isEqualTo("123");
  }

  @Test
  void testOfUUID() {
    UUID uuid = UUID.randomUUID();
    StateId id = StateId.of(uuid);
    Assertions.assertThat(id.value()).isEqualTo(uuid.toString());
  }

  @Test
  void exposesValue() {
    Assertions.assertThat(new StateId("id").value()).isEqualTo("id");
  }

  @Test
  void idEquality() {
    // Given
    var uuid = UUID.randomUUID();
    var uuidStr = uuid.toString();

    // When
    var id1 = StateId.of(uuid);
    var id2 = StateId.of(uuidStr);

    // Then
    Assertions.assertThat(id1).isEqualTo(id2);
    Assertions.assertThat(id2.value()).isEqualTo(uuidStr);
  }

  @Test
  void validation() {
    String str = null;
    //noinspection ConstantValue
    Assertions.assertThatNullPointerException().isThrownBy(() -> StateId.of(str));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> StateId.of(""));
  }

  @Test
  void convertsIdAndGeneratesRandomUuid() {
    //given
    var eventId = EventId.of("event-id");

    //when
    var converted = StateId.of(eventId);
    var random = StateId.randomUUID();

    //then
    Assertions.assertThat(converted.value()).isEqualTo(eventId.value());
    Assertions.assertThat(UUID.fromString(random.value())).isNotNull();
  }
}
