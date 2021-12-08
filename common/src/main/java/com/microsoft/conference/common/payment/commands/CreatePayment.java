package com.microsoft.conference.common.payment.commands;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.commanding.AbstractCommandMessage;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreatePayment extends AbstractCommandMessage<String> {
    private String orderId;
    private String conferenceId;
    private String description;
    private BigDecimal totalAmount;
    private List<PaymentLine> lines;
}
