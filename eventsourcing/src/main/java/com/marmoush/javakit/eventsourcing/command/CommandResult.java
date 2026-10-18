package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.core.domain.Result;
import com.marmoush.javakit.eventsourcing.event.Event;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Outcome of handling a command at the application layer.
 *
 * <ul>
 *   <li>{@link Accepted}: the command was applied; carries the events it produced. An empty event
 *       list is valid and means "accepted, nothing changed" (e.g. an idempotent command).
 *   <li>{@link Rejected}: a business rule refused the command; carries the {@link CommandFailure}.
 * </ul>
 *
 * Infrastructure and concurrency failures are not modelled here; they stay exceptions.
 */
public sealed interface CommandResult permits CommandResult.Accepted, CommandResult.Rejected {

  record Accepted(List<Event> events) implements CommandResult {
    public Accepted {
      events = List.copyOf(events);
    }
  }

  record Rejected(CommandFailure failure) implements CommandResult {
    public Rejected {
      Objects.requireNonNull(failure, "Failure is required");
    }
  }

  static CommandResult accepted(List<Event> events) {
    return new Accepted(events);
  }

  static CommandResult rejected(CommandFailure failure) {
    return new Rejected(failure);
  }

  default boolean isAccepted() {
    return this instanceof Accepted;
  }

  default boolean isRejected() {
    return this instanceof Rejected;
  }

  /** Collapses both outcomes into one type, e.g. an HTTP response. */
  default <T> T fold(
          Function<? super Accepted, ? extends T> onAccepted,
          Function<? super CommandFailure, ? extends T> onRejected) {
    return switch (this) {
      case Accepted accepted -> onAccepted.apply(accepted);
      case Rejected rejected -> onRejected.apply(rejected.failure());
    };
  }

  /** Converts to a {@link Result} of the produced events. */
  default Result<List<Event>, CommandFailure> toResult() {
    return switch (this) {
      case Accepted accepted -> Result.ok(accepted.events());
      case Rejected rejected -> Result.err(rejected.failure());
    };
  }
}