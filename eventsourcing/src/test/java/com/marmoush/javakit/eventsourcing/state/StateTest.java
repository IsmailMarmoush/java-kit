package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StateTest {
  @Test
  void exposesMetadataThroughConvenienceMethods() {
    //given
    var stateId = StateId.of("state");
    State state = new TestState(EventMeta.builder()
                                         .eventId(EventId.of("event"))
                                         .operationType(OperationType.EVOLUTION)
                                         .version(4)
                                         .stateId(stateId)
                                         .commandId(CommandId.of("command"))
                                         .build());

    Assertions.assertThat(state.meta().stateId()).isEqualTo(stateId);
    Assertions.assertThat(state.meta().version()).isEqualTo(4);
  }

  private record TestState(EventMeta meta) implements State {}
}
