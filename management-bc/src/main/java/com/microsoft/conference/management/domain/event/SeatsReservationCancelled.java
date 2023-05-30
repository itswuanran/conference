package com.microsoft.conference.management.domain.event;

import com.microsoft.conference.management.domain.model.SeatAvailableQuantity;
import lombok.Getter;
import lombok.Setter;
import org.enodeframework.eventing.AbstractDomainEventMessage;

import java.util.List;

@Getter
@Setter
public class SeatsReservationCancelled extends AbstractDomainEventMessage {
    private String reservationId;
    private List<SeatAvailableQuantity> seatAvailableQuantities;

    public SeatsReservationCancelled() {
    }

    public SeatsReservationCancelled(String reservationId, List<SeatAvailableQuantity> seatAvailableQuantities) {
        this.reservationId = reservationId;
        this.seatAvailableQuantities = seatAvailableQuantities;
    }
}
