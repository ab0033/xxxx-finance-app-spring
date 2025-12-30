package com.finance.port;


import event.LedgerEntryCreated;

@FunctionalInterface
public interface LedgerEventPublisher {
    void publish(LedgerEntryCreated event);
}
