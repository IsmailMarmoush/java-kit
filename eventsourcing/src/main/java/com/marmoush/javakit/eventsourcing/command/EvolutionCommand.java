package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.util.Optional;

public interface EvolutionCommand extends Command {
  @Override
  EvolutionCommandMeta meta();

  default CommandId commandId() {
    return meta().commandId();
  }

  default StateId stateId() {
    return meta().stateId();
  }

  @Override
  default Optional<EventId> sagaSource() {
    return meta().sagaSource();
  }
}
