package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionLogic;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.state.State;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.CreateState;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecisionLogic;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleEvolutionLogic;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandHandlerTest {

  @Test
  void appendsTheEventProducedForANewState() {
    // Given
    var stateId = StateId.of(0);
    var eventRepo = EventRepo.inMemory();
    var aggregate = newAggregate(stateId, eventRepo);

    // When
    var created = aggregate.handle(command(0, stateId));

    // Then
    Assertions.assertThat(created.get()).isInstanceOf(StateCreated.class);
    assertThat(eventRepo.size(stateId)).isOne();
  }

  @Test
  void doesNotAppendWhenDecisionIsRejected() {
    var stateId = StateId.of("rejected-state");
    var eventRepo = EventRepo.inMemory();
    DecisionLogic rejectingLogic = new DecisionLogic() {
      @Override
      public DecisionResult decide(Command command, EventMeta meta) {
        return DecisionResult.failure(() -> "Rejected");
      }

      @Override
      public DecisionResult decide(State state, Command command, EventMeta meta) {
        return DecisionResult.failure(() -> "Rejected");
      }
    };
    var handler = CommandHandler.create(new Decider(rejectingLogic, EventId::randomUUID),
                                        new Evolver(new SimpleEvolutionLogic()),
                                        eventRepo);

    var result = handler.handle(command(1, stateId));

    assertThat(result.isFailure()).isTrue();
    assertThat(eventRepo.fetch(stateId)).isEmpty();
  }

  private static CommandHandler newAggregate(StateId stateId, EventRepo eventRepo) {
    return CommandHandler.create(new Decider(new SimpleDecisionLogic(), () -> stateId),
                                 new Evolver(new SimpleEvolutionLogic()),
                                 eventRepo);
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
