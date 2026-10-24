package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

public class EvolutionEventMeta extends EventMeta {

  public EvolutionEventMeta(EventId id, long version, StateId stateId, CommandId commandId) {
    this(id, version, stateId, commandId, null);
  }

  public EvolutionEventMeta(EventId eventId, long version, StateId stateId, CommandId commandId, EventId sagaSource) {
    super(eventId, version, stateId, commandId, sagaSource);
  }
}

