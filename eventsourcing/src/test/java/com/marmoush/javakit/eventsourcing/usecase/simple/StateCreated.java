package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EventMeta;

public record StateCreated(EventMeta meta) implements CreationEvent {}
