package com.microsoft.conference.common.management.commands;

import lombok.Getter;
import lombok.Setter;
import org.enodeframework.commanding.AbstractCommandMessage;

import java.util.Date;

@Getter
@Setter
public class CreateConference extends AbstractCommandMessage {
    private String accessCode;
    private String ownerName;
    private String ownerEmail;
    private String slug;
    private String name;
    private String description;
    private String location;
    private String tagline;
    private String twitterSearch;
    private Date startDate;
    private Date endDate;
}
