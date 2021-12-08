package com.microsoft.conference.common.registration.commands.seatassignment;

import org.enodeframework.commanding.AbstractCommandMessage;

public class CreateSeatAssignments extends AbstractCommandMessage<String> {
    public CreateSeatAssignments() {
    }

    public CreateSeatAssignments(String orderId) {
        super(orderId);
    }
}
