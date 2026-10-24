package com.marmoush.javakit.eventsourcing.event;

import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;

public class CreationEventMeta extends EventMeta {

  public CreationEventMeta(EventId id, StateId stateId, CommandId commandId) {
    this(id, stateId, commandId, null);
  }

  public CreationEventMeta(EventId eventId, StateId stateId, CommandId commandId, EventId sagaSource) {
    super(eventId, 0, stateId, commandId, sagaSource);
  }
}

