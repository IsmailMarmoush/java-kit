package com.marmoush.javakit.eventsourcing.command;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandEntity<T> implements Command {
  CommandMeta meta;
  T state;

  @Builder
  public CommandEntity(@NonNull CommandMeta meta, T state) {
    this.meta = meta;
    this.state = state;
  }
}
