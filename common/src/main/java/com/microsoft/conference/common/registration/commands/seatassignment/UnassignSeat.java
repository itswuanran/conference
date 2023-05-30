package com.microsoft.conference.common.registration.commands.seatassignment;

import lombok.Data;
import org.enodeframework.commanding.AbstractCommandMessage;

@Data
public class UnassignSeat extends AbstractCommandMessage {
    private int position;

    public UnassignSeat() {
    }

    public UnassignSeat(String assignmentsId) {
        super(assignmentsId);
    }
}
