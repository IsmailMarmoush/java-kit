package com.marmoush.javakit.eventsourcing.command;

import java.io.Serializable;

public interface Command extends Serializable {
  CommandMeta meta();
}
