package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.SagaSourced;
import com.marmoush.javakit.eventsourcing.Versioned;

import java.io.Serializable;

public interface Event extends Partitioned, Versioned, SagaSourced, Serializable {
  EventMeta meta();
}
