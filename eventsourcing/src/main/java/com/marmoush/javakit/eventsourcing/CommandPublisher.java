package com.marmoush.javakit.eventsourcing;

import com.marmoush.javakit.eventsourcing.command.Command;

public interface CommandPublisher {
  void publish(Command command);
}
