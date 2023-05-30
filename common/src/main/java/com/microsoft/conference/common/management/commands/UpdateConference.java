package com.microsoft.conference.common.management.commands;

import org.enodeframework.commanding.AbstractCommandMessage;

import java.util.Date;

public class UpdateConference extends AbstractCommandMessage {
    public String name;
    public String description;
    public String location;
    public String tagline;
    public String twitterSearch;
    public Date startDate;
    public Date endDate;
    public Boolean isPublished;
}
