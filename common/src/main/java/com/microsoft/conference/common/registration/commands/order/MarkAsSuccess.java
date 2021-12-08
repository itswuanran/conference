package com.microsoft.conference.common.registration.commands.order;

import org.enodeframework.commanding.AbstractCommandMessage;

public class MarkAsSuccess extends AbstractCommandMessage<String> {
    public MarkAsSuccess() {
    }

    public MarkAsSuccess(String orderId) {
        super(orderId);
    }
}
