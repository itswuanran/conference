package com.microsoft.conference.common.registration.commands.order;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.commanding.AbstractCommandMessage;
import org.enodeframework.common.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlaceOrder extends AbstractCommandMessage {
    private String conferenceId;
    private List<SeatInfo> seatInfos;

    public PlaceOrder() {
        super(IdGenerator.id());
        seatInfos = new ArrayList<>();
    }
}
