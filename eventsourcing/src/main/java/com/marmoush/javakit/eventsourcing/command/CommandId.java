package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.DefaultId;
import com.marmoush.javakit.eventsourcing.Id;

import java.util.UUID;

public class CommandId extends DefaultId {

  public CommandId(String value) {
    super(value);
  }

  public static CommandId of(String value) {
    return new CommandId(value);
  }

  public static CommandId of(long value) {
    return new CommandId(String.valueOf(value));
  }

  public static CommandId of(UUID uuid) {
    return new CommandId(uuid.toString());
  }

  public static CommandId of(Id id) {
    return new CommandId(id.value());
  }

  public static CommandId randomUUID() {
    return CommandId.of(UUID.randomUUID().toString());
  }
}
