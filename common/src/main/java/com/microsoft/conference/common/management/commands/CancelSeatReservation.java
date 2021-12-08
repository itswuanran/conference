package com.microsoft.conference.common.management.commands;

import org.enodeframework.commanding.AbstractCommandMessage;

public class CancelSeatReservation extends AbstractCommandMessage<String> {
    public String reservationId;

    public CancelSeatReservation() {
    }

    public CancelSeatReservation(String conferenceId, String reservationId) {
        super(conferenceId);
        this.reservationId = reservationId;
    }
}
