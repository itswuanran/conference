package com.microsoft.conference.common.payment.message;

import org.enodeframework.messaging.AbstractApplicationMessage;

public abstract class PaymentMessage extends AbstractApplicationMessage {
    public String paymentId;
    public String orderId;
    public String conferenceId;
}
