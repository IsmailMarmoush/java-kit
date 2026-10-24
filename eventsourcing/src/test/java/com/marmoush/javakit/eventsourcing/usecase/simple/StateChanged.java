package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;

public record StateChanged(EventMeta meta) implements EvolutionEvent {}
