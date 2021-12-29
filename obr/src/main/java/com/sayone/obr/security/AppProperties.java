package com.sayone.obr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
@Component
public class AppProperties {

    @Autowired
    private Environment env;

    public String getTokenSecret(){
        return  env.getProperty("tokenSecret");
    }
    public String getFromAddress() {
        return  env.getProperty("fromAddress");
    }
    public String getSenderName(){
        return env.getProperty("senderName");
    }
     public String getSubject1(){
        return env.getProperty("subject1");
     }
     public String getSubject2(){
        return env.getProperty("subject2");
     }
     public String getSubject3(){
        return env.getProperty("subject3");
     }

}
