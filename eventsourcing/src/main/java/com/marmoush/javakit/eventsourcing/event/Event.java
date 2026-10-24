package com.marmoush.javakit.eventsourcing.event;

import java.io.Serializable;

public interface Event extends Serializable {
  EventMeta meta();
}
