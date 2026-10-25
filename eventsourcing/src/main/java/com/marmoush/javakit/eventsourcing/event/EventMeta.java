package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.OperationType;
import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.command.CommandId;
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
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventMeta implements Partitioned, Versioned, SagaSourced{
  @Serial
  private static final long serialVersionUID = 0L;

  EventId eventId;
  OperationType operationType;
  long version;
  StateId stateId;
  CommandId commandId;
  @Getter(AccessLevel.NONE)
  EventId sagaSource;

  @Builder
  public EventMeta(@NonNull EventId eventId,
                   @NonNull OperationType operationType,
                   long version,
                   @NonNull StateId stateId,
                   @NonNull CommandId commandId,
                   EventId sagaSource) {
    if (version < 0) {
      throw new IllegalArgumentException("version can't be less than zero");
    }
    this.operationType = operationType;
    this.eventId = Objects.requireNonNull(eventId);
    this.version = version;
    this.stateId = Objects.requireNonNull(stateId);
    this.commandId = Objects.requireNonNull(commandId);
    this.sagaSource = sagaSource;
  }

  @Override
  public Optional<EventId> sagaSource() {
    return Optional.ofNullable(sagaSource);
  }
}

