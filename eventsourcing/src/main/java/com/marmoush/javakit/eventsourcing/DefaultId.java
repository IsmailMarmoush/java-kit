package com.marmoush.javakit.eventsourcing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@ToString
public class DefaultId implements Id {

  String value;

  protected DefaultId(@NonNull String value) {
    if (value.isBlank()) {
      throw new IllegalArgumentException("Value cannot be blank");
    }
    this.value = value;
  }
}
