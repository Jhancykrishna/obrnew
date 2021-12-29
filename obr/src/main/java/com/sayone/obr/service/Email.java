package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.repository.DownloadRepository;
import com.sayone.obr.repository.EmailRepository;
import com.sayone.obr.security.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

@Service
public class Email {

    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    AppProperties appProperties;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public Email(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail1(UserDto user, Long bookId) throws MessagingException, UnsupportedEncodingException {
        String bookName = downloadRepository.findBookName(bookId);
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("bookName", bookName);
        String process = templateEngine.process("emails/initialDownload", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        Helper.message(appProperties.getFromAddress(),appProperties.getSenderName(),user.getEmail(),String.format(appProperties.getSubject1(),bookName), process,message);
        javaMailSender.send(message);
        emailReport(user, appProperties.getSubject1());

    }
    public void sendEmail2(UserDto user, Long bookId, Long dnoRemain) throws MessagingException, UnsupportedEncodingException {
        String bookName = downloadRepository.findBookName(bookId);
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("bookName", bookName);
        context.setVariable("dnoRemain", dnoRemain);
        String process = templateEngine.process("emails/downloadAgain", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        Helper.message(appProperties.getFromAddress(),appProperties.getSenderName(),user.getEmail(),String.format(appProperties.getSubject2(),bookName), process,message);
        javaMailSender.send(message);
        emailReport(user, appProperties.getSubject2());

    }
    public void sendEmail3(UserDto user) throws MessagingException, UnsupportedEncodingException {

        Context context = new Context();
        context.setVariable("user", user);
        String process = templateEngine.process("emails/outOfDownloads", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        Helper.message(appProperties.getFromAddress(),appProperties.getSenderName(),user.getEmail(),appProperties.getSubject3(), process,message);
        javaMailSender.send(message);
        emailReport(user, appProperties.getSubject3());
    }
    private void emailReport(UserDto user,String subject){
        String toAddress = user.getEmail();
        EmailEntity emailReport = new EmailEntity();
        emailReport.setFromAddress(appProperties.getFromAddress());
        emailReport.setToAddress(toAddress);
        emailReport.setSubject(subject);
        emailReport.setDate(LocalDate.now());
        emailRepository.save(emailReport);
    }

}