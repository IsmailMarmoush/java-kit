package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;
import com.marmoush.javakit.eventsourcing.event.EventMeta;

import java.io.Serializable;

public interface State extends Serializable {
  EventMeta meta();
}
