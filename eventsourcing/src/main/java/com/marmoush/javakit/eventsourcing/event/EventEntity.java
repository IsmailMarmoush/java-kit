package com.marmoush.javakit.eventsourcing.event;

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
public class EventEntity<T> implements Event {
  EventMeta meta;
  T state;

  @Builder
  public EventEntity(@NonNull EventMeta meta, T state) {
    this.meta = meta;
    this.state = state;
  }
}
