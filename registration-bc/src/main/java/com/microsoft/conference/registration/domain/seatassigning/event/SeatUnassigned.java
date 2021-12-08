package com.microsoft.conference.registration.domain.seatassigning.event;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

@Getter
@Setter
public class SeatUnassigned extends AbstractDomainEventMessage<String> {
    private int position;

    public SeatUnassigned() {
    }

    public SeatUnassigned(int position) {
        this.position = position;
    }
}
