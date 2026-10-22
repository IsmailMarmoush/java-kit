package com.marmoush.javakit.eventsourcing.id;

import java.io.Serializable;
import java.util.UUID;

public interface Id extends Serializable {
  String value();

  static Id randomUUID() {
    return DefaultId.of(UUID.randomUUID().toString());
  }
}
