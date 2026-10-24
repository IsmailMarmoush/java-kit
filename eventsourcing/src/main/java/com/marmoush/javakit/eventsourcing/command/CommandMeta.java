package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.event.EventId;
import com.marmoush.javakit.eventsourcing.state.StateId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandMeta implements SagaSourced, Serializable {
  @Serial
  private static final long serialVersionUID = 0L;

  CommandId commandId;
  OperationType operationType;
  StateId stateId;
  @Getter(AccessLevel.NONE)
  EventId sagaSource;

  @Builder
  public CommandMeta(@NonNull CommandId commandId,
                     @NonNull StateId stateId,
                     @NonNull OperationType operationType,
                     EventId sagaSource) {
    this.commandId = Objects.requireNonNull(commandId);
    this.sagaSource = sagaSource;
    this.stateId = stateId;
    this.operationType = operationType;
  }

  @Override
  public Optional<EventId> sagaSource() {
    return Optional.ofNullable(sagaSource);
  }
}

