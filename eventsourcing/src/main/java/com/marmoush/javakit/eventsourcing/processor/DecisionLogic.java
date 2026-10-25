package com.marmoush.javakit.eventsourcing.processor;

import com.marmoush.javakit.eventsourcing.command.Command;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.state.State;

public interface DecisionLogic {
  DecisionResult decide(Command command, EventMeta meta);

  DecisionResult decide(State state, Command command, EventMeta meta);
}
