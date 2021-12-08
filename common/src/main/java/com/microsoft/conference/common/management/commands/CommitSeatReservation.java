package com.microsoft.conference.common.management.commands;

import org.enodeframework.commanding.AbstractCommandMessage;

public class CommitSeatReservation extends AbstractCommandMessage<String> {
    public String reservationId;

    public CommitSeatReservation() {
    }

    public CommitSeatReservation(String conferenceId, String reservationId) {
        super(conferenceId);
        this.reservationId = reservationId;
    }
}
