package com.marmoush.javakit.eventsourcing.command;

public interface CommandFailure {
  String message();

  Command command();
}
