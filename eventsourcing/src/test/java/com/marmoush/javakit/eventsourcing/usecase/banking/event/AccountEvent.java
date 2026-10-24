package com.marmoush.javakit.eventsourcing.usecase.banking.event;

import com.marmoush.javakit.eventsourcing.event.Event;

public sealed interface AccountEvent extends Event permits AccountClosed,
                                                           AccountCreated,
                                                           ClosureRejected,
                                                           CreditRejected,
                                                           Credited,
                                                           DebitConfirmed,
                                                           DebitRejected,
                                                           Debited,
                                                           NameChanged {}
