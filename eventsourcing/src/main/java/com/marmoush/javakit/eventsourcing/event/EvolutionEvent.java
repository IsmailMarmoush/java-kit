package com.marmoush.javakit.eventsourcing.event;

public interface EvolutionEvent extends Event {
  @Override
  EvolutionEventMeta meta();
}
