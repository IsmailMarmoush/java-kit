package com.marmoush.javakit.eventsourcing.command.repo;

import com.marmoush.javakit.eventsourcing.command.Command;

public interface CommandPublisher {
  void publish(Command command);
}
