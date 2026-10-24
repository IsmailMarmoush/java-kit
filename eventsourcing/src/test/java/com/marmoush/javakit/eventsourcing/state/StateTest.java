package com.marmoush.javakit.eventsourcing.state;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StateTest {
  @Test
  void exposesMetadataThroughConvenienceMethods() {
    //given
    var stateId = StateId.of("state");
    State state = new TestState(new StateMeta(stateId, 4));

    //when
    var partitionKey = state.pKey();

    //then
    Assertions.assertThat(state.stateId()).isEqualTo(stateId);
    Assertions.assertThat(state.version()).isEqualTo(4);
    Assertions.assertThat(partitionKey.value()).isEqualTo(stateId.value());
    Assertions.assertThat(state.withMeta(new StateMeta(stateId, 5)).version()).isEqualTo(5);
  }

  private record TestState(StateMeta meta) implements State {
    @Override
    public State withMeta(StateMeta newMeta) {
      return new TestState(newMeta);
    }
  }
}
