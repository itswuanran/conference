package com.microsoft.conference.common.management.commands;

import org.enodeframework.commanding.AbstractCommandMessage;

import java.util.ArrayList;
import java.util.List;

public class MakeSeatReservation extends AbstractCommandMessage {
    public String reservationId;
    public List<SeatReservationItemInfo> seats;

    public MakeSeatReservation() {
    }

    public MakeSeatReservation(String conferenceId) {
        super(conferenceId);
        this.seats = new ArrayList<>();
    }
}
