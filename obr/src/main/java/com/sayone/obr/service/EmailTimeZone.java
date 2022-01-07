package com.sayone.obr.service;


import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.security.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class EmailTimeZone {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private  TemplateEngine templateEngine;
    @Autowired
    AppProperties appProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MailProperties mailProperties;

    public void sendMail(Long id) throws MessagingException {
        UserEntity userEntity = userRepository.findAllById(id);
        String toEmail= userEntity.getEmail();
        String user = userEntity.getFirstName();
        Date date = userEntity.getDateCreated();
        String timeZone = userEntity.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        TimeZone tzOfUser = TimeZone.getTimeZone(timeZone);
        sdf.setTimeZone(tzOfUser);
        String sDateOfUser = sdf.format(date);

        sendMail(mailProperties.getUsername(),toEmail,tzOfUser,sDateOfUser,user);

    }
    private void sendMail(String fromEmail,String toEmail,TimeZone tzOfUser,String sDateOfUser,String user) throws MessagingException {

        Context context = new Context();
        context.setVariable("UserTz", tzOfUser.getID());
        context.setVariable("DisplayName", tzOfUser.getDisplayName());
        context.setVariable("TzDate", sDateOfUser);
        context.setVariable("User", user);
        MimeMessage message = javaMailSender.createMimeMessage();
        String process = templateEngine.process("emails/timezone", context);

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setSubject(appProperties.getSubject4());
        messageHelper.setText(process, true);
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(toEmail);
        javaMailSender.send(message);
    }


}
