package com.marmoush.javakit.eventsourcing.usecase.banking;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.command.CommandId;
import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.usecase.banking.command.Debit;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.AccountEvent;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.DebitRejected;
import com.marmoush.javakit.eventsourcing.usecase.banking.event.Debited;
import com.marmoush.javakit.eventsourcing.usecase.banking.state.OpenAccount;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AccountDeciderTest {

  @ParameterizedTest
  @ValueSource(ints = {300, 500, 600})
  void debit(int debitAmount) {
    // Given
    int balance = 500;
    var stateMeta = EventMeta.builder()
                             .eventId(EventId.of("created"))
                             .operationType(OperationType.CREATION)
                             .stateId(BankingFixtures.ALICE_ID)
                             .commandId(CommandId.of("create"))
                             .build();
    var openAccount = new OpenAccount(stateMeta, BankingFixtures.ALICE, balance);
    var commandMeta = CommandMeta.builder()
                                 .commandId(CommandId.randomUUID())
                                 .stateId(BankingFixtures.ALICE_ID)
                                 .operationType(OperationType.EVOLUTION)
                                 .build();
    var debit = new Debit(commandMeta, BankingFixtures.BOB_ID, debitAmount);

    // When
    var event = new Decider(new AccountDecisionLogic(), () -> BankingFixtures.ALICE_ID).decide(openAccount, debit).get();
    var accountEvent = (AccountEvent) event;

    // Then
    assertThat(accountEvent.meta().stateId()).isEqualTo(BankingFixtures.ALICE_ID);
    if (debitAmount < balance) {
      assertThat(event).isInstanceOf(Debited.class);
    } else {
      assertThat(event).isInstanceOf(DebitRejected.class);
    }
  }
}
