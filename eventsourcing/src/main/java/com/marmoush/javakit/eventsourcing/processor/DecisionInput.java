package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.state.State;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class DecisionInput {
  private final Command command;
  @Getter(AccessLevel.NONE)
  private final State state;

  @Builder
  public DecisionInput(@NonNull Command command, State state) {
    this.command = command;
    this.state = state;
  }

  public Optional<State> state() {
    return Optional.ofNullable(state);
  }
}
