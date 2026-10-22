package com.marmoush.javakit.eventsourcing.id;

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
  void idsCompareByValueAndConcreteType() {
    //given
    Id id = new DefaultId("same");
    var equalId = new DefaultId("same");
    var differentType = new StateId("same");

    //when
    var equal = id.equals(equalId);

    //then
    Assertions.assertThat(equal).isTrue();
    Assertions.assertThat(id).hasSameHashCodeAs(equalId);
    Assertions.assertThat(id.equals(id)).isTrue();
    Assertions.assertThat(id.equals(null)).isFalse();
    Assertions.assertThat(id.equals(differentType)).isFalse();
    Assertions.assertThat(id).isNotEqualTo(new DefaultId("different"));
    Assertions.assertThatIllegalArgumentException().isThrownBy(() -> new DefaultId(" \t "));
  }
}
