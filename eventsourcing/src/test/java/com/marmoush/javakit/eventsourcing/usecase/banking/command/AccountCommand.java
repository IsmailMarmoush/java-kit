package com.marmoush.javakit.eventsourcing.usecase.banking.command;

import com.marmoush.javakit.eventsourcing.command.Command;

public sealed interface AccountCommand extends Command
        permits ChangeName, CloseAccount, CreateAccount, Credit, Debit {}
