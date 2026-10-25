package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.event.EventMeta;

public interface State  {
  EventMeta meta();
}
