package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.CreationCommandMeta;

public record CreateState(CreationCommandMeta meta) implements CreationCommand {}
