package com.marmoush.javakit.eventsourcing.usecase.banking.command;

import com.marmoush.javakit.eventsourcing.command.CommandMeta;
import com.marmoush.javakit.eventsourcing.id.StateId;

public record Credit(CommandMeta meta, StateId debitedAcc, long amount) implements AccountCommand {}
