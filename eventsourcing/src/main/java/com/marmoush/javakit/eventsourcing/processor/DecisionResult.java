package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.core.domain.Result;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult.DecisionEvent;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult.DecisionFailure;
import com.marmoush.javakit.eventsourcing.error.ESDomainError;
import com.marmoush.javakit.eventsourcing.event.Event;

import java.util.Objects;
import java.util.function.Function;

/**
 * Outcome of handling a command at the application layer.
 *
 * <ul>
 *   <li>{@link DecisionEvent}: the command was applied; carries the events it produced. An empty event
 *       list is valid and means "accepted, nothing changed" (e.g. an idempotent command).
 *   <li>{@link DecisionFailure}: a business rule refused the command; carries the {@link ESDomainError}.
 * </ul>
 * <p>
 * Infrastructure and concurrency failures are not modelled here; they stay exceptions.
 */
public sealed interface DecisionResult permits DecisionEvent, DecisionFailure {

  record DecisionEvent(Event event) implements DecisionResult {
    public static DecisionEvent of(Event event) {
      return new DecisionEvent(event);
    }

    public DecisionEvent {
      Objects.requireNonNull(event, "Event is required");
    }
  }

  record DecisionFailure(ESDomainError failure) implements DecisionResult {
    public static DecisionFailure of(ESDomainError failure) {
      return new DecisionFailure(failure);
    }

    public DecisionFailure {
      Objects.requireNonNull(failure, "Failure is required");
    }
  }

  static DecisionResult of(Event events) {
    return DecisionResult.success(events);
  }

  static DecisionResult of(ESDomainError failure) {
    return DecisionResult.failure(failure);
  }

  static DecisionResult success(Event events) {
    return new DecisionEvent(events);
  }

  static DecisionResult failure(ESDomainError failure) {
    return new DecisionFailure(failure);
  }

  default boolean isSuccess() {
    return this instanceof DecisionEvent;
  }

  default boolean isFailure() {
    return this instanceof DecisionFailure;
  }

  default Event get() {
    if (this instanceof DecisionEvent(Event event)) {
      return event;
    }
    throw new IllegalStateException("%s isn't instance of Event".formatted(this));
  }

  default ESDomainError error() {
    if (this instanceof DecisionFailure(ESDomainError error)) {
      return error;
    }
    throw new IllegalStateException("%s isn't instance of DecisionError".formatted(this));
  }

  default <T> T fold(Function<? super DecisionEvent, ? extends T> onAccepted,
                     Function<? super ESDomainError, ? extends T> onRejected) {
    return switch (this) {
      case DecisionEvent decisionEvent -> onAccepted.apply(decisionEvent);
      case DecisionFailure(ESDomainError error) -> onRejected.apply(error);
    };
  }

  default Result<Event, ESDomainError> toResult() {
    return switch (this) {
      case DecisionEvent(Event event) -> Result.success(event);
      case DecisionFailure(ESDomainError failure) -> Result.failure(failure);
    };
  }
}