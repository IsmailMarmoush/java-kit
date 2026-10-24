package com.marmoush.javakit.eventsourcing.id;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PartitionIdTest {
  @Test
  void createsPartitionIdsFromSupportedValues() {
    //given
    var uuid = UUID.randomUUID();
    Id id = new StateId("state");

    //when
    var fromString = PartitionId.of("partition");
    var fromLong = PartitionId.of(12L);
    var fromUuid = PartitionId.of(uuid);
    var fromId = PartitionId.of(id);
    var random = PartitionId.randomUUID();

    //then
    Assertions.assertThat(fromString.value()).isEqualTo("partition");
    Assertions.assertThat(fromLong.value()).isEqualTo("12");
    Assertions.assertThat(fromUuid.value()).isEqualTo(uuid.toString());
    Assertions.assertThat(fromId.value()).isEqualTo(id.value());
    Assertions.assertThat(UUID.fromString(random.value())).isNotNull();
  }
}
