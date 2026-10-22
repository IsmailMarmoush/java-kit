package com.marmoush.javakit.eventsourcing.id;

import java.io.Serial;
import java.util.Objects;

class DefaultId implements Id {
  @Serial
  private static final long serialVersionUID = 0L;

  private final String value;

  DefaultId(String value) {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException("Value cannot be blank");
    }
    this.value = value;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    DefaultId defaultId = (DefaultId) o;
    return Objects.equals(value, defaultId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
