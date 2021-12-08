package com.microsoft.conference.common.management.message;

import org.enodeframework.messaging.AbstractApplicationMessage;

public class SeatInsufficientMessage extends AbstractApplicationMessage {
    public String conferenceId;
    public String reservationId;
}
