package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.core.domain.Result;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.error.ESDomainError;
import com.marmoush.javakit.eventsourcing.error.MismatchingCommandStateIdError;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.state.State;

import java.util.function.Supplier;

public interface Decider {
  Supplier<EventId> eventIdSupplier();

  Supplier<Long> timeSupplier();

  /**
   * @param command   the incoming command
   * @param eventMeta the new EventMeta
   * @return a new Event with eventMeta as its value
   */
  DecisionResult decide(Command command, EventMeta eventMeta);

  /**
   * @param state     the initial state
   * @param command   the incoming command
   * @param eventMeta the new EventMeta
   * @return a CommandResult with Event(eventMeta) as its value if accepted
   *
   */
  DecisionResult decide(State state, Command command, EventMeta eventMeta);

  default DecisionResult decide(Command command) {
    return decide(command, eventMeta(command));
  }

  default DecisionResult decide(State state, Command command) {
    return switch (eventMeta(state, command)) {
      case Result.Failure(ESDomainError error) -> DecisionResult.failure(error);
      case Result.Success(EventMeta event) -> decide(state, command, event);
    };
  }

  default EventMeta eventMeta(Command command) {
    return new EventMeta(eventIdSupplier().get(),
                         0,
                         command.meta().stateId(),
                         command.meta().commandId(),
                         timeSupplier().get(),
                         command.meta().sagaSource().orElse(null));
  }

  default Result<EventMeta, ESDomainError> eventMeta(State state, Command command) {
    if (!state.meta().stateId().equals(command.meta().stateId())) {
      return Result.failure(MismatchingCommandStateIdError.of(state.meta().stateId(), command));
    }
    var eMeta = new EventMeta(eventIdSupplier().get(),
                              state.meta().version() + 1,
                              state.meta().stateId(),
                              command.meta().commandId(),
                              timeSupplier().get(),
                              command.meta().sagaSource().orElse(null));
    return Result.success(eMeta);
  }
}
