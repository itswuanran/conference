package com.microsoft.conference.management.domain.event;

import org.enodeframework.eventing.AbstractDomainEventMessage;

public class ConferencePublished extends AbstractDomainEventMessage<String> {
    public ConferencePublished() {
    }
}
