package com.microsoft.conference.management.domain.event;

import com.microsoft.conference.management.domain.model.ConferenceEditableInfo;
import org.enodeframework.eventing.AbstractDomainEventMessage;

public class ConferenceUpdated extends AbstractDomainEventMessage {
    private ConferenceEditableInfo info;

    public ConferenceUpdated() {
    }

    public ConferenceUpdated(ConferenceEditableInfo info) {
        this.info = info;
    }

    public ConferenceEditableInfo getInfo() {
        return info;
    }

    public void setInfo(ConferenceEditableInfo info) {
        this.info = info;
    }
}
