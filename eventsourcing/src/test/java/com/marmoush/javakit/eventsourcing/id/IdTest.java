package com.marmoush.javakit.eventsourcing.id;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class IdTest {
  @Test
  void randomUuidCreatesUuidBackedId() {
    //given

    //when
    var id = Id.randomUUID();

    //then
    Assertions.assertThat(UUID.fromString(id.value())).isNotNull();
  }

  @Test
  void idsCompareByValue() {
    //given
    Id id = new StateId("same");
    var equalId = StateId.of("same");
    var differentType = CommandId.of("same");

    //when
    var equal = id.equals(equalId);

    //then
    Assertions.assertThat(equal).isTrue();
    Assertions.assertThat(id).hasSameHashCodeAs(equalId).isEqualTo(differentType).isNotEqualTo(StateId.of("different"));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> StateId.of(" \t "));
  }
}
