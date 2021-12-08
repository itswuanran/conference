package com.microsoft.conference.payments.domain.event;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

@Getter
@Setter
public class PaymentRejected extends AbstractDomainEventMessage<String> {
    private String orderId;
    private String conferenceId;

    public PaymentRejected() {
    }

    public PaymentRejected(String orderId, String conferenceId) {
        this.orderId = orderId;
        this.conferenceId = conferenceId;
    }
}
