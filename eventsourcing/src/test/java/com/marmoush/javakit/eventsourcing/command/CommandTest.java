package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandTest {
  @Test
  void exposesMetadataThroughConvenienceMethods() {
    //given
    var stateId = StateId.of("state");
    var commandId = CommandId.of("command");
    var sagaSource = EventId.of("source");
    Command command = new TestCommand(new CommandMeta(commandId, stateId, 123L, sagaSource));

    //when
    var partitionKey = command.pKey();

    //then
    Assertions.assertThat(command.commandId()).isEqualTo(commandId);
    Assertions.assertThat(command.stateId()).isEqualTo(stateId);
    Assertions.assertThat(command.sagaSource()).contains(sagaSource);
    Assertions.assertThat(partitionKey.value()).isEqualTo(stateId.value());
    Assertions.assertThat(new TestCommand(new CommandMeta(commandId, stateId)).sagaSource()).isEmpty();
  }

  private record TestCommand(CommandMeta meta) implements Command {}
}
