package com.microsoft.conference.management.domain.event;

import com.microsoft.conference.management.domain.model.SeatTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.DomainEvent;

@Getter
@Setter
public abstract class SeatTypeEvent extends DomainEvent<String> {
    private String seatTypeId;
    private SeatTypeInfo seatTypeInfo;

    public SeatTypeEvent() {
    }

    public SeatTypeEvent(String seatTypeId, SeatTypeInfo seatTypeInfo) {
        this.seatTypeId = seatTypeId;
        this.seatTypeInfo = seatTypeInfo;
    }
}
