package com.microsoft.conference.registration.domain.order.event;

import com.microsoft.conference.registration.domain.order.model.Registrant;
import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

@Getter
@Setter
public class OrderRegistrantAssigned extends AbstractDomainEventMessage<String> {
    private String conferenceId;
    private Registrant registrant;

    public OrderRegistrantAssigned() {
    }

    public OrderRegistrantAssigned(String conferenceId, Registrant registrant) {
        this.conferenceId = conferenceId;
        this.registrant = registrant;
    }
}
