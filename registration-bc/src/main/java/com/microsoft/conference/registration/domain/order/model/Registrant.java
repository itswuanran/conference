package com.microsoft.conference.registration.domain.order.model;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.common.utils.Assert;

@Getter
@Setter
public class Registrant {
    private String firstName;
    private String lastName;
    private String email;

    public Registrant() {
    }

    public Registrant(String firstName, String lastName, String email) {
        Assert.nonNull(firstName, "firstName");
        Assert.nonNull(lastName, "lastName");
        Assert.nonNull(email, "email");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
