package com.marmoush.javakit.eventsourcing.command;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;

public class CreationCommandMeta extends CommandMeta {

  public CreationCommandMeta(CommandId commandId) {
    super(commandId, null);
  }

  public CreationCommandMeta(CommandId commandId, EventId sagaSource) {
    super(commandId, sagaSource);
  }
}

