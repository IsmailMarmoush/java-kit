package com.marmoush.javakit.eventsourcing.id;

import java.util.UUID;

public interface Id {
  String value();

  static Id randomUUID() {
    return DefaultId.of(UUID.randomUUID().toString());
  }
}
