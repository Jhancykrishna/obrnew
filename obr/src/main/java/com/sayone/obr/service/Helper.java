package com.sayone.obr.service;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class Helper {

    public static void message(String fromAddress, String sendName, String email, String subject, String process, MimeMessage message) throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, sendName);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(process, true);
    }


}
