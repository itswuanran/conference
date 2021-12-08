package com.microsoft.conference.common.management.message;

import org.enodeframework.messaging.AbstractApplicationMessage;

import java.util.List;

public class SeatsReservedMessage extends AbstractApplicationMessage {
    public String conferenceId;
    public String reservationId;
    public List<SeatReservationItem> reservationItems;
}
