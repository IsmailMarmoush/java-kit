package com.marmoush.javakit.eventsourcing.id;

import java.util.UUID;

public class EventId extends DefaultId {

  public EventId(String value) {
    super(value);
  }

  public static EventId of(String value) {
    return new EventId(value);
  }

  public static EventId of(long value) {
    return new EventId(String.valueOf(value));
  }

  public static EventId of(UUID uuid) {
    return new EventId(uuid.toString());
  }

  public static EventId of(Id id) {
    return new EventId(id.value());
  }

  public static EventId randomUUID() {
    return EventId.of(UUID.randomUUID().toString());
  }
}
