package com.microsoft.conference.registration.domain.order.event;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

@Getter
@Setter
public abstract class OrderEvent extends AbstractDomainEventMessage<String> {
    private String conferenceId;

    public OrderEvent() {
    }

    public OrderEvent(String conferenceId) {
        this.conferenceId = conferenceId;
    }
}
