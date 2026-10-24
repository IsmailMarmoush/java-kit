package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventTest {
  @Test
  void exposesMetadataThroughConvenienceMethods() {
    //given
    var stateId = StateId.of("state");
    var eventId = EventId.of("event");
    var commandId = CommandId.of("command");
    var sagaSource = EventId.of("source");
    Event event = new TestEvent(new EventMeta(eventId, 3, stateId, commandId, 123L, sagaSource));

    //when
    var partitionKey = event.pKey();

    //then
    Assertions.assertThat(event.eventId()).isEqualTo(eventId);
    Assertions.assertThat(event.commandId()).isEqualTo(commandId);
    Assertions.assertThat(event.stateId()).isEqualTo(stateId);
    Assertions.assertThat(event.sagaSource()).contains(sagaSource);
    Assertions.assertThat(event.version()).isEqualTo(3);
    Assertions.assertThat(partitionKey.value()).isEqualTo(stateId.value());
    Assertions.assertThat(new TestEvent(new EventMeta(eventId, 0, stateId, commandId)).sagaSource()).isEmpty();
  }

  private record TestEvent(EventMeta meta) implements Event {}
}
