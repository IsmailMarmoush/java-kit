package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;
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
@Builder
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class CommandMeta implements SagaSourced, Serializable {
  @Serial
  private static final long serialVersionUID = 0L;

  @NonNull
  CommandId commandId;

  @NonNull
  OperationType operationType;

  @NonNull
  StateId stateId;

  @Getter(AccessLevel.NONE)
  EventId sagaSource;

  public CommandMeta(CommandId commandId, EventId sagaSource, StateId stateId, OperationType operationType) {
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

