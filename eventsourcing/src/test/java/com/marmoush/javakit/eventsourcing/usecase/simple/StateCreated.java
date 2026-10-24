package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.CreationEventMeta;
import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventMeta;

public record StateCreated(CreationEventMeta meta) implements CreationEvent {}
