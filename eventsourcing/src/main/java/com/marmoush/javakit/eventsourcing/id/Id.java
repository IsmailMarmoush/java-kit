package com.marmoush.javakit.eventsourcing.id;

import com.marmoush.javakit.core.domain.ValueObject;

import java.io.Serializable;
import java.util.UUID;

public interface Id extends Serializable, ValueObject<String> {
  static Id randomUUID() {
    return new DefaultId(UUID.randomUUID().toString());
  }
}
