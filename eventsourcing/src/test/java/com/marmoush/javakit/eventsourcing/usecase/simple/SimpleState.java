package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.State;

public record SimpleState(EventMeta meta) implements State {
  public SimpleState withMeta(EventMeta newMeta) {
    return new SimpleState(newMeta);
  }
}
