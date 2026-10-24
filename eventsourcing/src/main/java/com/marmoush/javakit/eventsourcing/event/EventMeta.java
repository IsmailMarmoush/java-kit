package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class EventMeta implements Partitioned, Versioned, SagaSourced, Serializable {
  @Serial
  private static final long serialVersionUID = 0L;
  private final EventId eventId;
  private final long version;
  private final StateId stateId;
  private final CommandId commandId;
  private final EventId sagaSource;

  public EventMeta(EventId eventId, long version, StateId stateId, CommandId commandId, EventId sagaSource) {
    if (version < 0) {
      throw new IllegalArgumentException("version can't be less than zero");
    }
    this.eventId = Objects.requireNonNull(eventId);
    this.version = version;
    this.stateId = Objects.requireNonNull(stateId);
    this.commandId = Objects.requireNonNull(commandId);
    this.sagaSource = sagaSource;
  }

  public EventId eventId() {return eventId;}

  @Override
  public long version() {return version;}

  @Override
  public StateId stateId() {return stateId;}

  public CommandId commandId() {return commandId;}

  @Override
  public Optional<EventId> sagaSource() {return Optional.ofNullable(sagaSource);}

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventMeta eventMeta = (EventMeta) o;
    return version == eventMeta.version
           && Objects.equals(eventId, eventMeta.eventId)
           && Objects.equals(stateId,
                             eventMeta.stateId)
           && Objects.equals(commandId, eventMeta.commandId)
           && Objects.equals(sagaSource, eventMeta.sagaSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, version, stateId, commandId, sagaSource);
  }

  @Override
  public String toString() {
    String msg = "EventMeta{eventId=%s, version=%d, stateId=%s, commandId=%s, sagaSource=%s}";
    return msg.formatted(eventId, version, stateId, commandId, sagaSource);
  }
}

