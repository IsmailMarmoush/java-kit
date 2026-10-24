package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.StateId;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountSagaTest {
  @Test
  void evolve() {
    // Given
    StateId debitedAccount = BankingFixtures.ALICE_ID;
    StateId creditedAcc = BankingFixtures.BOB_ID;

    var sagaEventId = EventId.of("SomeSagaEventId");
    var commandId = CommandId.randomUUID();
    var eventId = EventId.randomUUID();
    var eventMetaWithSaga = EventMeta.builder()
                                     .eventId(eventId)
                                     .operationType(OperationType.EVOLUTION)
                                     .version(1)
                                     .stateId(debitedAccount)
                                     .commandId(commandId)
                                     .sagaSource(sagaEventId)
                                     .build();

    var debited = new Debited(eventMetaWithSaga, creditedAcc, 300);

    // When
    var command = BankingFixtures.saga.react(debited);

    // Then
    Assertions.assertThat(command).isPresent();
    Assertions.assertThat(command.get()).isInstanceOf(Credit.class);
    // And
    var credit = (Credit) command.get();
    Assertions.assertThat(credit.meta().stateId()).isEqualTo(creditedAcc);
    Assertions.assertThat(credit.debitedAcc()).isEqualTo(debitedAccount);
  }
}
