package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandIdTest {
  @Test
  void testOfString() {
    CommandId id = CommandId.of("123");
    assertEquals("123", id.value());
  }

  @Test
  void testOfLong() {
    CommandId id = CommandId.of(123L);
    assertEquals("123", id.value());
  }

  @Test
  void testOfUUID() {
    UUID uuid = UUID.randomUUID();
    CommandId id = CommandId.of(uuid);
    assertEquals(uuid.toString(), id.value());
  }

  @Test
  void checkToString() {
    Assertions.assertThat(new CommandId("id")).hasToString("id");
  }

  @Test
  void idEquality() {
    // Given
    var uuid = UUID.randomUUID();
    var uuidStr = uuid.toString();

    // When
    var id1 = CommandId.of(uuid);
    var id2 = CommandId.of(uuidStr);

    // Then
    Assertions.assertThat(id1).isEqualTo(id2).hasToString(uuidStr);
  }

  @Test
  void validation() {
    String str = null;
    //noinspection ConstantValue
    Assertions.assertThatNullPointerException().isThrownBy(() -> CommandId.of(str));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> CommandId.of(""));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> CommandId.of(" \t "));
  }

  @Test
  void randomUuidCreatesUuidBackedId() {
    //given

    //when
    var id = CommandId.randomUUID();

    //then
    Assertions.assertThat(UUID.fromString(id.value())).isNotNull();
  }
}
