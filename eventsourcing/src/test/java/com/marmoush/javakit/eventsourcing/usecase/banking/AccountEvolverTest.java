package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.marmoush.javakit.eventsourcing.usecase.banking.BankingFixtures.ALICE;
import static com.marmoush.javakit.eventsourcing.usecase.banking.BankingFixtures.ALICE_ID;
import static com.marmoush.javakit.eventsourcing.usecase.banking.BankingFixtures.BOB_ID;
import static com.marmoush.javakit.eventsourcing.usecase.banking.BankingFixtures.evolver;

class AccountEvolverTest {
  @Test
  void evolve() {
    // Given
    var createdMeta = EventMeta.builder()
                               .eventId(EventId.of("created"))
                               .operationType(OperationType.CREATION)
                               .stateId(ALICE_ID)
                               .commandId(CommandId.of("create"))
                               .build();
    var openAccount = new OpenAccount(createdMeta, ALICE, 500);
    var debitedMeta = EventMeta.builder()
                               .eventId(EventId.randomUUID())
                               .operationType(OperationType.EVOLUTION)
                               .version(1)
                               .stateId(ALICE_ID)
                               .commandId(CommandId.randomUUID())
                               .build();
    var debited = new Debited(debitedMeta, BOB_ID, 300);

    // When
    var acc = (OpenAccount) evolver.evolve(openAccount, debited);

    // Then
    Assertions.assertThat(acc.balance()).isEqualTo(200);
  }
}
