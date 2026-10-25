package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.core.domain.ValueObject;

import java.util.UUID;

public interface Id extends ValueObject<String> {
  static Id randomUUID() {
    return new DefaultId(UUID.randomUUID().toString());
  }
}
