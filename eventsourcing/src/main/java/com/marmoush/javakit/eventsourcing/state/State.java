package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.event.EventMeta;

import java.io.Serializable;

public interface State extends Serializable {
  EventMeta meta();
}
