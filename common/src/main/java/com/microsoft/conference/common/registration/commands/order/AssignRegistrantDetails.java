package com.microsoft.conference.common.registration.commands.order;

import lombok.Data;
import org.enodeframework.commanding.AbstractCommandMessage;

@Data
public class AssignRegistrantDetails extends AbstractCommandMessage<String> {
    private String firstName;
    private String lastName;
    private String email;

    public AssignRegistrantDetails() {
    }

    public AssignRegistrantDetails(String orderId) {
        super(orderId);
    }
}
