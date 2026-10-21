package com.marmoush.javakit.eventsourcing.error;

import java.util.Objects;

public class DefaultESDomainError implements ESDomainError {
  private final String message;

  DefaultESDomainError(String message) {
    this.message = message;
  }

  @Override
  public String message() {return message;}

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    DefaultESDomainError that = (DefaultESDomainError) o;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message);
  }

  @Override
  public String toString() {
    return "DefaultCommandFailure{" + "message='" + message + '\'' + '}';
  }
}
