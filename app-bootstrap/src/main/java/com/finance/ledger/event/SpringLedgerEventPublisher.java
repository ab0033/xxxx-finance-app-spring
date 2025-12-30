package com.finance.ledger.event;

import event.LedgerEntryCreated;
import com.finance.port.LedgerEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringLedgerEventPublisher implements LedgerEventPublisher {
    private final ApplicationEventPublisher spring;

    @Override
    public void publish(LedgerEntryCreated event) {
        spring.publishEvent(event);
    }
}
