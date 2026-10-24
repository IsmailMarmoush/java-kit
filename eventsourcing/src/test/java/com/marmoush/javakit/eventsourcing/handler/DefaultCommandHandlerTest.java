package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.CreateState;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCommandHandlerTest {

  @Test
  void appendsTheEventProducedForANewState() {
    // Given
    var stateId = StateId.of(0);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);

    // When
    var created = aggregate.handle(command(0, stateId));

    // Then
    assertThat(created.get()).isInstanceOf(StateCreated.class);
    assertThat(eventRepo.size(stateId)).isOne();
  }

  private static DefaultCommandHandler newAggregate(StateId stateId, EventRepo eventRepo) {
    var decider = new SimpleDecider(() -> stateId);
    return new DefaultCommandHandler(decider, new SimpleEvolver(), eventRepo);
  }

  @SuppressWarnings("SameParameterValue")
  private static CreateState command(int commandId, StateId stateId) {
    return new CreateState(CommandMeta.builder()
                                      .commandId(CommandId.of(commandId))
                                      .stateId(stateId)
                                      .operationType(OperationType.CREATION)
                                      .build());
  }
}
