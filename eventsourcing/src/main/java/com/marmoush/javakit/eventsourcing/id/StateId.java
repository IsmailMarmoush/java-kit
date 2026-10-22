package com.marmoush.javakit.eventsourcing.id;

import java.util.UUID;

public class StateId extends DefaultId {

  public StateId(String value) {
    super(value);
  }

  public static StateId of(String value) {
    return new StateId(value);
  }

  public static StateId of(long value) {
    return new StateId(String.valueOf(value));
  }

  public static StateId of(UUID uuid) {
    return new StateId(uuid.toString());
  }

  public static StateId of(Id id) {
    return new StateId(id.value());
  }

  public static StateId randomUUID() {
    return StateId.of(UUID.randomUUID().toString());
  }
}
