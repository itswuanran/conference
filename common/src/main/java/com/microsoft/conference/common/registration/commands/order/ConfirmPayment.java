package com.microsoft.conference.common.registration.commands.order;

import org.enodeframework.commanding.AbstractCommandMessage;

public class ConfirmPayment extends AbstractCommandMessage {
    public boolean isPaymentSuccess;

    public ConfirmPayment() {
    }

    public ConfirmPayment(String orderId, boolean isPaymentSuccess) {
        super(orderId);
        this.isPaymentSuccess = isPaymentSuccess;
    }
}
