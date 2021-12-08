package com.microsoft.conference.common.registration.commands.order;

import org.enodeframework.commanding.AbstractCommandMessage;

public class ConfirmReservation extends AbstractCommandMessage<String> {
    public boolean isReservationSuccess;

    public ConfirmReservation() {
    }

    public ConfirmReservation(String orderId, boolean isReservationSuccess) {
        super(orderId);
        this.isReservationSuccess = isReservationSuccess;
    }
}
