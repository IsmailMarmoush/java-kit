package com.marmoush.javakit.eventsourcing.exception;

import com.marmoush.javakit.eventsourcing.state.State;

public final class UnhandledStateTypeException extends RuntimeException {
  private static final String MSG = "Unhandled State: %s[%s] implementation";

  public static UnhandledStateTypeException of(State state) {
    return new UnhandledStateTypeException(state);
  }

  private UnhandledStateTypeException(State state) {
    super(MSG.formatted(state.getClass().getName(), state.meta()));
  }
}
