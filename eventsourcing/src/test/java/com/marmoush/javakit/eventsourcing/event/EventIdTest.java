package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.id.EventId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventIdTest {
  @Test
  void testOfString() {
    EventId id = EventId.of("123");
    assertEquals("123", id.value());
  }

  @Test
  void testOfLong() {
    EventId id = EventId.of(123L);
    assertEquals("123", id.value());
  }

  @Test
  void testOfUUID() {
    UUID uuid = UUID.randomUUID();
    EventId id = EventId.of(uuid);
    assertEquals(uuid.toString(), id.value());
  }

  @Test
  void checkToString() {
    Assertions.assertThat(new EventId("id")).hasToString("id");
  }

  @Test
  void idEquality() {
    // Given
    var uuid = UUID.randomUUID();
    var uuidStr = uuid.toString();

    // When
    var id1 = EventId.of(uuid);
    var id2 = EventId.of(uuidStr);

    // Then
    Assertions.assertThat(id1).isEqualTo(id2).hasToString(uuidStr);
  }

  @Test
  void validation() {
    String str = null;
    //noinspection ConstantValue
    Assertions.assertThatNullPointerException().isThrownBy(() -> EventId.of(str));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> EventId.of(""));
  }
}
