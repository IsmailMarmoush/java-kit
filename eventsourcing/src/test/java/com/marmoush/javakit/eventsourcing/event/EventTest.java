package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.OperationType;
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

    // When
    Event event = new TestEvent(eventMeta(eventId, 3, stateId, commandId, sagaSource));

    // Then
    Assertions.assertThat(event.meta().eventId()).isEqualTo(eventId);
    Assertions.assertThat(event.meta().commandId()).isEqualTo(commandId);
    Assertions.assertThat(event.meta().stateId()).isEqualTo(stateId);
    Assertions.assertThat(event.meta().version()).isEqualTo(3);
    Assertions.assertThat(event.meta().sagaSource()).contains(sagaSource);
    Assertions.assertThat(eventMeta(eventId, 0, stateId, commandId, null).sagaSource()).isEmpty();
  }

  private static EventMeta eventMeta(EventId eventId,
                                     long version,
                                     StateId stateId,
                                     CommandId commandId,
                                     EventId sagaSource) {
    return EventMeta.builder()
                    .eventId(eventId)
                    .operationType(version == 0 ? OperationType.CREATION : OperationType.EVOLUTION)
                    .version(version)
                    .stateId(stateId)
                    .commandId(commandId)
                    .sagaSource(sagaSource)
                    .build();
  }

  private record TestEvent(EventMeta meta) implements Event {}
}
