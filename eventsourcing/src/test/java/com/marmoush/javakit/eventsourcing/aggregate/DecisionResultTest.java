package com.marmoush.javakit.eventsourcing.aggregate;

import com.marmoush.javakit.core.domain.Result;
import com.marmoush.javakit.eventsourcing.DecisionResult;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.error.ESDomainError;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.event.Event;

import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.event.EventMeta;

import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DecisionResultTest {
  private final Event event = new StateCreated(new EventMeta(EventId.of(0), 0, StateId.of(0), CommandId.of(0), 0));
  private final ESDomainError error = () -> "Rejected";

  @Test
  void successFactoriesAndAccessors() {
    // Given
    // When
    var of = DecisionResult.of(event);
    var success = DecisionResult.success(event);

    // Then
    assertThat(of).isEqualTo(success).isInstanceOf(DecisionEvent.class);
    assertThat(of.isSuccess()).isTrue();
    assertThat(of.isFailure()).isFalse();
    assertThat(of.get()).isSameAs(event);
    assertThat(DecisionEvent.of(event).event()).isSameAs(event);
  }

  @Test
  void failureFactoriesAndAccessors() {
    // Given
    // When
    var of = DecisionResult.of(error);
    var failure = DecisionResult.failure(error);

    // Then
    assertThat(of).isEqualTo(failure).isInstanceOf(DecisionFailure.class);
    assertThat(of.isSuccess()).isFalse();
    assertThat(of.isFailure()).isTrue();
    assertThat(of.error()).isSameAs(error);
    assertThat(DecisionFailure.of(error).failure()).isSameAs(error);
  }

  @Test
  void rejectsNullEvent() {
    // When / Then
    assertThatNullPointerException().isThrownBy(() -> new DecisionEvent(null)).withMessage("Event is required");
  }

  @Test
  void rejectsNullFailure() {
    // When / Then
    assertThatNullPointerException().isThrownBy(() -> new DecisionFailure(null)).withMessage("Failure is required");
  }

  @Test
  void getThrowsForFailure() {
    // Given
    var result = DecisionResult.failure(error);

    // When / Then
    assertThatIllegalStateException().isThrownBy(result::get)
                                     .withMessage("%s isn't instance of Event".formatted(result));
  }

  @Test
  void errorThrowsForSuccess() {
    // Given
    var result = DecisionResult.success(event);

    // When / Then
    assertThatIllegalStateException().isThrownBy(result::error)
                                     .withMessage("%s isn't instance of DecisionError".formatted(result));
  }

  @Test
  void foldAppliesMatchingHandler() {
    // Given
    var accepted = DecisionResult.success(event);
    var rejected = DecisionResult.failure(error);

    // When / Then
    assertThat(accepted.fold(_ -> "accepted", ESDomainError::message)).isEqualTo("accepted");
    assertThat(rejected.fold(_ -> "accepted", ESDomainError::message)).isEqualTo("Rejected");
  }

  @Test
  void toResultPreservesSuccessAndFailureValues() {
    // Given
    var accepted = DecisionResult.success(event);
    var rejected = DecisionResult.failure(error);

    // When
    var success = accepted.toResult();
    var failure = rejected.toResult();

    // Then
    assertThat(success).isEqualTo(new Result.Success<>(event));
    assertThat(failure).isEqualTo(new Result.Failure<>(error));
  }
}
