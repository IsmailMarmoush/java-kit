package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandTest {
  @Test
  void exposesMetadataThroughConvenienceMethods() {
    //given
    var stateId = StateId.of("state");
    var commandId = CommandId.of("command");
    var sagaSource = EventId.of("source");
    Command command = new TestCommand(CommandMeta.builder()
                                                 .commandId(commandId)
                                                 .stateId(stateId)
                                                 .operationType(OperationType.EVOLUTION)
                                                 .sagaSource(sagaSource)
                                                 .build());

    //then
    Assertions.assertThat(command.meta().commandId()).isEqualTo(commandId);
    Assertions.assertThat(command.meta().stateId()).isEqualTo(stateId);
    Assertions.assertThat(command.meta().sagaSource()).contains(sagaSource);
    Assertions.assertThat(new TestCommand(CommandMeta.builder()
                                                     .commandId(commandId)
                                                     .stateId(stateId)
                                                     .operationType(OperationType.CREATION)
                                                     .build()).meta().sagaSource()).isEmpty();
  }

  private record TestCommand(CommandMeta meta) implements Command {}
}
