package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.SagaSourced;

import java.io.Serializable;

public interface Command extends SagaSourced, Serializable {
  CommandMeta meta();
}
