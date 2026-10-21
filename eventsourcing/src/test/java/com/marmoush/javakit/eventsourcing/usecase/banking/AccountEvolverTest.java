package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.command.CommandIds;
import com.marmoush.javakit.eventsourcing.event.EventIds;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.StateMeta;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.marmoush.javakit.eventsourcing.usecase.banking.TestData.ALICE;
import static com.marmoush.javakit.eventsourcing.usecase.banking.TestData.ALICE_ID;
import static com.marmoush.javakit.eventsourcing.usecase.banking.TestData.BOB_ID;
import static com.marmoush.javakit.eventsourcing.usecase.banking.TestData.evolver;
import static java.util.UUID.randomUUID;

class AccountEvolverTest {
  @Test
  void evolve() {
    // Given
    var openAccount = new OpenAccount(new StateMeta(ALICE_ID), ALICE, 500);
    var debited = new Debited(new EventMeta(EventIds.of(randomUUID()), 1, ALICE_ID, CommandIds.of(randomUUID())),
                              BOB_ID,
                              300);

    // When
    var acc = (OpenAccount) evolver.evolve(openAccount, debited);

    // Then
    Assertions.assertThat(acc.balance()).isEqualTo(200);
  }
}
