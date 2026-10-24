package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

public class EvolutionCommandMeta extends CommandMeta {
  private final StateId stateId;

  public EvolutionCommandMeta(CommandId commandId, StateId stateId) {
    super(commandId, null);
    this.stateId = stateId;
  }

  public EvolutionCommandMeta(CommandId commandId, StateId stateId, EventId sagaSource) {
    super(commandId, sagaSource);
    this.stateId = stateId;
  }

  public StateId stateId() {
    return stateId;
  }
}

