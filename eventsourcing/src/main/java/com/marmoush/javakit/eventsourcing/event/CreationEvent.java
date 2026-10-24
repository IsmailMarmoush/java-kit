package com.marmoush.javakit.eventsourcing.event;

public interface CreationEvent extends Event {
  @Override
  CreationEventMeta meta();
}
