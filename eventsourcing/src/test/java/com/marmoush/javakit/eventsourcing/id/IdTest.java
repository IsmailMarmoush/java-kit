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
}
