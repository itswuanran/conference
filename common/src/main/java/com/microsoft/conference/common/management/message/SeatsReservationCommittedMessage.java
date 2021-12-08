package com.microsoft.conference.common.management.message;

import org.enodeframework.messaging.AbstractApplicationMessage;

public class SeatsReservationCommittedMessage extends AbstractApplicationMessage {
    public String conferenceId;
    public String reservationId;
}
