package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.event.EventMeta;
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
public class StateEntity<T> implements State {
  EventMeta meta;
  T state;

  @Builder
  public StateEntity(@NonNull EventMeta meta, T state) {
    this.meta = meta;
    this.state = state;
  }
}
