package com.marmoush.javakit.eventsourcing.handler;

import com.marmoush.javakit.eventsourcing.processor.Decider;
import com.marmoush.javakit.eventsourcing.processor.DecisionResult;
import com.marmoush.javakit.eventsourcing.processor.Evolver;
import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.repo.EventRepo;

public interface CommandHandler {
  DecisionResult handle(Command command);

  static CommandHandler create(Decider decider, Evolver evolver, EventRepo eventRepo) {
    return new DefaultCommandHandler(decider, evolver, eventRepo);
  }
}
