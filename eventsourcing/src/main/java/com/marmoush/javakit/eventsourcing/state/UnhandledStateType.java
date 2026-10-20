package com.marmoush.javakit.eventsourcing.state;

public final class UnhandledStateType extends RuntimeException {
  private static final String MSG = "Unhandled State: %s[%s] implementation";

  public static UnhandledStateType of(State state) {
    return new UnhandledStateType(state);
  }

  private UnhandledStateType(State state) {
    super(MSG.formatted(state.getClass().getName(), state.meta()));
  }
}
