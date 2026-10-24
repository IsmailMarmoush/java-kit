package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommandMeta;

public record ChangeState(EvolutionCommandMeta meta) implements EvolutionCommand {}
