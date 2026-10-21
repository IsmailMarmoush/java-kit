package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.core.id.Ids;
import com.marmoush.javakit.eventsourcing.aggregate.error.DuplicateCommand;
import com.marmoush.javakit.eventsourcing.aggregate.error.MismatchingCommandStateIdError;
import com.marmoush.javakit.eventsourcing.command.CommandIds;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.EventIds;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.state.StateIds;
import com.marmoush.javakit.eventsourcing.usecase.simple.ChangeState;
import com.marmoush.javakit.eventsourcing.usecase.simple.CreateState;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecider;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolver;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateChanged;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SyncAggregateTest {

  @Test
  void handlesCreationAndSubsequentEvolution() {
    // Given
    var stateId = StateIds.of(0);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);

    // When
    var created = aggregate.handle(command(0, stateId));
    var changed = aggregate.handle(new ChangeState(new CommandMeta(CommandIds.of(1), stateId)));

    // Then
    assertThat(created.get()).isInstanceOf(StateCreated.class);
    assertThat(changed.get()).isInstanceOf(StateChanged.class);
    assertThat(changed.get().version()).isOne();
    assertThat(eventRepo.size(stateId)).isEqualTo(2);
  }

  @Test
  void rejectsDuplicateCommandWithoutAppendingAnotherEvent() {
    // Given
    var stateId = StateIds.of(0);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);
    var command = command(0, stateId);

    // When
    aggregate.handle(command);
    var duplicate = aggregate.handle(command);

    // Then
    assertThat(duplicate.error()).isInstanceOf(DuplicateCommand.class);
    assertThat(eventRepo.size(stateId)).isOne();
  }

  @Test
  void rejectsCommandForDifferentState() {
    // Given
    var stateId = StateIds.of(0);
    var otherStateId = StateIds.of(1);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);

    // When
    var result = aggregate.handle(command(0, otherStateId));

    // Then
    assertThat(result.error()).isInstanceOf(MismatchingCommandStateIdError.class);
    assertThat(eventRepo.size(stateId)).isZero();
  }

  @Test
  void rejectsCommandWithPreviouslyProcessedSagaSource() {
    // Given
    var stateId = StateIds.of(0);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);
    var sagaSource = EventIds.of(100);
    aggregate.handle(command(0, stateId));
    aggregate.handle(new ChangeState(new CommandMeta(CommandIds.of(1), stateId, 0L, sagaSource)));

    // When
    var result = aggregate.handle(new ChangeState(new CommandMeta(CommandIds.of(2), stateId, 0L, sagaSource)));

    // Then
    assertThat(result.error()).isInstanceOf(DuplicateCommand.class);
    assertThat(eventRepo.size(stateId)).isEqualTo(2);
  }

  private static SyncAggregate newAggregate(StateId stateId, EventRepo eventRepo) {
    var decider = new SimpleDecider(() -> Ids.of(0), () -> 0L);
    return new SyncAggregate(stateId, decider, new SimpleEvolver(), eventRepo);
  }

  @SuppressWarnings("SameParameterValue")
  private static CreateState command(int commandId, StateId stateId) {
    return new CreateState(new CommandMeta(CommandIds.of(commandId), stateId));
  }
}
