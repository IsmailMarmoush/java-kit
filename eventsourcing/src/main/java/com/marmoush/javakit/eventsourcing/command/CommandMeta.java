package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public abstract class CommandMeta implements Serializable {
  @Serial
  private static final long serialVersionUID = 0L;
  private final CommandId commandId;
  private final EventId sagaSource;

  public CommandMeta(CommandId commandId, EventId sagaSource) {
    this.commandId = Objects.requireNonNull(commandId);
    this.sagaSource = sagaSource;
  }

  public CommandId commandId() {return commandId;}

  public Optional<EventId> sagaSource() {return Optional.ofNullable(sagaSource);}

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommandMeta that = (CommandMeta) o;
    return Objects.equals(commandId, that.commandId) && Objects.equals(sagaSource, that.sagaSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandId, sagaSource);
  }

  @Override
  public String toString() {
    String msg = "CommandMeta{commandId=%s, sagaSource=%s}";
    return msg.formatted(commandId, sagaSource);
  }
}

