package com.microsoft.conference.management.domain.event;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

@Getter
@Setter
public class SeatTypeRemoved extends AbstractDomainEventMessage {
    private String seatTypeId;

    public SeatTypeRemoved() {
    }

    public SeatTypeRemoved(String seatTypeId) {
        this.seatTypeId = seatTypeId;
    }
}
