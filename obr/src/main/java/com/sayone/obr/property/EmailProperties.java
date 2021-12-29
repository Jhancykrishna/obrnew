package com.sayone.obr.property;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EmailProperties {


    public String fromAddress = "jhancykrishna1@gmail.com";
    public String senderName = "OBR";
    public String subject1 = " Your first download";
    public String subject2 = "Welcome Back";
    public String subject3 = "Out of Downloads" ;


    public String getSubject1() {
        return subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public String getSubject3() {
        return subject3;
    }

    public String getFromAddress(String fromAddress) {
        return fromAddress;
    }

    public String getSenderName(String senderName) {
        return senderName;
    }


}
