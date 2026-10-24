package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.processor.Saga;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.ChangeState;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class SagaCommandHandlerTest {
  private final Event event = new StateCreated(EventMeta.builder()
                                                        .eventId(EventId.of(0))
                                                        .operationType(OperationType.CREATION)
                                                        .stateId(StateId.of(0))
                                                        .commandId(CommandId.of(0))
                                                        .build());

  @Test
  void publishesAndReturnsCommandProducedBySaga() {
    // Given
    Command command = new ChangeState(CommandMeta.builder()
                                                 .commandId(CommandId.of(0))
                                                 .stateId(StateId.of(0))
                                                 .operationType(OperationType.EVOLUTION)
                                                 .build());
    List<Command> publishedCommands = new ArrayList<>();
    var handler = new SagaHandler(sagaReacting(ignored -> Optional.of(command)), publishedCommands::add);

    // When
    var result = handler.react(event);

    // Then
    assertThat(result).contains(command);
    assertThat(publishedCommands).containsExactly(command);
  }

  @Test
  void doesNotPublishWhenSagaProducesNoCommand() {
    // Given
    List<Command> publishedCommands = new ArrayList<>();
    var handler = new SagaHandler(sagaReacting(ignored -> Optional.empty()), publishedCommands::add);

    // When
    var result = handler.react(event);

    // Then
    assertThat(result).isEmpty();
    assertThat(publishedCommands).isEmpty();
  }

  private static Saga sagaReacting(Function<Event, Optional<Command>> reaction) {
    return new Saga(() -> CommandId.of(0)) {
      @Override
      public Optional<Command> react(Event event) {
        return reaction.apply(event);
      }
    };
  }
}
