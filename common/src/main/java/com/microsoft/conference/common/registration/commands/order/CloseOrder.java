package com.microsoft.conference.common.registration.commands.order;

import org.enodeframework.commanding.AbstractCommandMessage;

public class CloseOrder extends AbstractCommandMessage {
    public CloseOrder() {
    }

    public CloseOrder(String orderId) {
        super(orderId);
    }
}
