package com.sayone.obr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Autowired
    private Environment env;

    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }

    public String getFromAddress() {
        return env.getProperty("fromAddress");
    }

    public String getSenderName() {
        return env.getProperty("senderName");
    }

    public String getSubjectOut() {
        return env.getProperty("subject.out");
    }

    public String getSubjectAgain() {
        return env.getProperty("subject.again");
    }

    public String getSubjectInitial() {
        return env.getProperty("subject.initial");
    }

    public String getSubjectDownload() {
        return env.getProperty("subject.download");
    }

    public String getSubjectNewUsers() {
        return env.getProperty("subject.newUsers");
    }
}
