package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventMetaTest {
  private final StateId stateId = StateId.of(0);

  @Test
  void holdsRequiredMetadataAndOptionalSagaSource() {
    var meta = eventMeta(EventId.of(12), 7, stateId, CommandId.of(34), EventId.of(56));
    var same = eventMeta(EventId.of(12), 7, stateId, CommandId.of(34), EventId.of(56));
    Assertions.assertThat(meta.eventId()).isEqualTo(EventId.of(12));
    Assertions.assertThat(meta.operationType()).isEqualTo(OperationType.EVOLUTION);
    Assertions.assertThat(meta.version()).isEqualTo(7);
    Assertions.assertThat(meta.stateId()).isEqualTo(stateId);
    Assertions.assertThat(meta.commandId()).isEqualTo(CommandId.of(34));
    Assertions.assertThat(meta.sagaSource()).contains(EventId.of(56));
    Assertions.assertThat(meta).isEqualTo(same).hasSameHashCodeAs(same);
    Assertions.assertThat(eventMeta(EventId.of(13), 7, stateId, CommandId.of(34), null).sagaSource()).isEmpty();
  }

  @Test
  void wrongVersion() {
    Assertions.assertThatIllegalArgumentException()
              .isThrownBy(() -> eventMeta(EventId.of(0), -1, stateId, CommandId.of(0), null));
  }

  @Test
  void equalityIncludesEachMetadataField() {
    var meta = eventMeta(EventId.of(1), 2, stateId, CommandId.of(3), EventId.of(5));
    Assertions.assertThat(meta)
              .isNotEqualTo(eventMeta(EventId.of(9), 2, stateId, CommandId.of(3), EventId.of(5)))
              .isNotEqualTo(eventMeta(EventId.of(1), 8, stateId, CommandId.of(3), EventId.of(5)))
              .isNotEqualTo(eventMeta(EventId.of(1), 2, StateId.of(1), CommandId.of(3), EventId.of(5)))
              .isNotEqualTo(eventMeta(EventId.of(1), 2, stateId, CommandId.of(9), EventId.of(5)))
              .isNotEqualTo(eventMeta(EventId.of(1), 2, stateId, CommandId.of(3), EventId.of(9)));
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
}
