package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.id.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateIdTest {
  @Test
  void testOfString() {
    StateId id = StateId.of("123");
    assertEquals("123", id.value());
  }

  @Test
  void testOfLong() {
    StateId id = StateId.of(123L);
    assertEquals("123", id.value());
  }

  @Test
  void testOfUUID() {
    UUID uuid = UUID.randomUUID();
    StateId id = StateId.of(uuid);
    assertEquals(uuid.toString(), id.value());
  }

  @Test
  void checkToString() {
    Assertions.assertThat(new StateId("id")).hasToString("id");
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
    Assertions.assertThat(id1).isEqualTo(id2).hasToString(uuidStr);
  }

  @Test
  void validation() {
    String str = null;
    //noinspection ConstantValue
    Assertions.assertThatNullPointerException().isThrownBy(() -> StateId.of(str));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> StateId.of(""));
  }
}
