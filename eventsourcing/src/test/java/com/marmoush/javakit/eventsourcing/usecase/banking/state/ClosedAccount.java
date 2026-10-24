package com.marmoush.javakit.eventsourcing.usecase.banking.state;

import com.marmoush.javakit.eventsourcing.event.EventMeta;

public record ClosedAccount(EventMeta meta) implements Account {}
