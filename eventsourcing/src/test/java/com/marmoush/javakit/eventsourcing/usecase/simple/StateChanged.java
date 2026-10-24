package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEventMeta;

public record StateChanged(EvolutionEventMeta meta) implements EvolutionEvent {}
