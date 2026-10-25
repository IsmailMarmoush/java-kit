package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.Id;
import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.exception.DecisionException;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.ChangeState;
import com.marmoush.javakit.eventsourcing.usecase.simple.CreateState;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleDecisionLogic;
import com.marmoush.javakit.eventsourcing.usecase.simple.SimpleState;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateChanged;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeciderTest {
  private final Decider decider = new Decider(new SimpleDecisionLogic(), EventId::randomUUID);

  @Test
  void decideCreation() {
    // Given
    var createState = new CreateState(commandMeta(StateId.of(0)));

    // When
    var event = decider.decide(createState).get();

    // Then
    assertThat(event).isInstanceOf(StateCreated.class);
    assertThat(event.meta().version()).isZero();
  }

  @Test
  void creationMetadataUsesSuppliedIdsAndPreservesCommandMetadata() {
    var stateId = StateId.of("original-state");
    var sagaSource = EventId.of("saga-source");
    var command = new CreateState(CommandMeta.builder()
                                             .commandId(CommandId.of("command"))
                                             .stateId(stateId)
                                             .operationType(OperationType.CREATION)
                                             .sagaSource(sagaSource)
                                             .build());
    Iterator<Id> ids = List.<Id>of(EventId.of("new-event"), StateId.of("new-state")).iterator();
    var deterministicDecider = new Decider(new SimpleDecisionLogic(), ids::next);

    var event = deterministicDecider.decide(command).get();

    assertThat(event.meta().eventId()).isEqualTo(EventId.of("new-event"));
    assertThat(event.meta().stateId()).isEqualTo(StateId.of("new-state"));
    assertThat(event.meta().commandId()).isEqualTo(command.meta().commandId());
    assertThat(event.meta().operationType()).isEqualTo(command.meta().operationType());
    assertThat(event.meta().sagaSource()).contains(sagaSource);
  }

  @Test
  void decideEvolution() {
    // Given
    var stateId = StateId.of(0);
    var someState = new SimpleState(eventMeta(stateId, 0));
    var changeState = new ChangeState(commandMeta(stateId));

    // When
    var event = decider.decide(someState, changeState).get();

    // Then
    assertThat(event).isInstanceOf(StateChanged.class);
    assertThat(event.meta().version()).isOne();
  }

  @Test
  void decideEvolutionFail() {
    // Given
    var someState = new SimpleState(eventMeta(StateId.of("stateId"), 0));
    var changeState = new ChangeState(commandMeta(StateId.of("differentStateId")));

    assertThatThrownBy(() -> decider.decide(someState, changeState)).isInstanceOf(DecisionException.class)
                                                                    .hasMessageContaining("Command stateId mismatch");
  }

  private static CommandMeta commandMeta(StateId stateId) {
    return CommandMeta.builder()
                      .commandId(CommandId.of(0))
                      .stateId(stateId)
                      .operationType(OperationType.EVOLUTION)
                      .build();
  }

  private static EventMeta eventMeta(StateId stateId, long version) {
    return EventMeta.builder()
                    .eventId(EventId.of(version))
                    .operationType(OperationType.CREATION)
                    .version(version)
                    .stateId(stateId)
                    .commandId(CommandId.of(0))
                    .build();
  }
}
