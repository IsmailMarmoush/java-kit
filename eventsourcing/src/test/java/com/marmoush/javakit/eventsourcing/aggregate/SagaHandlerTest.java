package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.core.id.Id;
import com.marmoush.javakit.core.id.Ids;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandIds;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventIds;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.StateIds;
import com.marmoush.javakit.eventsourcing.usecase.simple.ChangeState;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class SagaHandlerTest {
  private final Event event = new StateCreated(new EventMeta(EventIds.of(0), 0, StateIds.of(0), CommandIds.of(0)));

  @Test
  void publishesAndReturnsCommandProducedBySaga() {
    // Given
    Command command = new ChangeState(new CommandMeta(CommandIds.of(0), StateIds.of(0)));
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
    return new Saga() {
      @Override
      public Supplier<Id> idSupplier() {
        return () -> Ids.of(0);
      }

      @Override
      public Supplier<Long> timeSupplier() {
        return () -> 0L;
      }

      @Override
      public Optional<Command> react(Event event) {
        return reaction.apply(event);
      }
    };
  }
}
