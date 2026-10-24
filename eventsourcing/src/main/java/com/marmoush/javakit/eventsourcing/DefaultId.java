package com.marmoush.javakit.eventsourcing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@ToString
public class DefaultId implements Id {
  @Serial
  private static final long serialVersionUID = 0L;

  String value;

  protected DefaultId(@NonNull String value) {
    if (value.isBlank()) {
      throw new IllegalArgumentException("Value cannot be blank");
    }
    this.value = value;
  }
}
