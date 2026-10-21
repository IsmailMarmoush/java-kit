package com.marmoush.javakit.eventsourcing.usecase.banking;



import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Credit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.UUID.randomUUID;

class AccountSagaTest {
  @Test
  void evolve() {
    // Given
    StateId debitedAccount = TestData.ALICE_ID;
    StateId creditedAcc = TestData.BOB_ID;

    var sagaEventId = EventId.of("SomeSagaEventId");
    var commandId = CommandId.of(randomUUID());
    var eventId = EventId.of(randomUUID());
    var eventMetaWithSaga = new EventMeta(eventId, 1, debitedAccount, commandId, 0, sagaEventId);

    var debited = new Debited(eventMetaWithSaga, creditedAcc, 300);

    // When
    var command = TestData.saga.react(debited);

    // Then
    Assertions.assertThat(command).isPresent();
    Assertions.assertThat(command.get()).isInstanceOf(Credit.class);
    // And
    var credit = (Credit) command.get();
    Assertions.assertThat(credit.accountId()).isEqualTo(creditedAcc);
    Assertions.assertThat(credit.debitedAcc()).isEqualTo(debitedAccount);
  }
}
