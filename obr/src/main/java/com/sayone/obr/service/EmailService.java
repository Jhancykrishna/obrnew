package com.sayone.obr.service;

import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import com.sayone.obr.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;



@Service
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private Environment env;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(UserDetailsRequestModel userDetails) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", userDetails);

        String process = templateEngine.process("emails/welcome", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome " + userDetails.getFirstName() + " " + userDetails.getLastName());
        helper.setText(process, true);
        helper.setTo(userDetails.getEmail());
        javaMailSender.send(mimeMessage);
        return "Sent";
    }

    void sendEmail1(String toAddress, String subject, String process, String bookLink, String bookName, String date, MimeMessage message, FileSystemResource file, MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {

        mime(toAddress, subject, process, bookName,helper, file,message);
        System.out.println("the file is " + file);
        System.out.println("1st mail");
        javaMailSender.send(message);
        System.out.println("send success");
        emailReport(toAddress,subject,date);
    }

    void sendEmail2(String subject, String process, String toAddress, String bookLink, String bookName, Long bookIdSend, String date, MimeMessage message, FileSystemResource file, MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {

        mime(toAddress, subject, process, bookName,helper, file, message);
        System.out.println("the book id and file is " + file + " " + bookIdSend + " " + bookLink);
        javaMailSender.send(message);
        emailReport(toAddress,subject,date);
    }

    void sendEmail3(String subject, String process, String toAddress, String date, MimeMessage message, MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {

        mimeOut(toAddress, subject, process,helper);
        javaMailSender.send(message);
        emailReport(toAddress,subject,date);
        throw new UserServiceException(DownloadErrors.DOWNLOAD_LIMIT.getErrorMessage());
    }

    private void mime(String toAddress, String subject, String process, String bookName, MimeMessageHelper helper, FileSystemResource file, MimeMessage message) throws MessagingException, UnsupportedEncodingException {

        mimeOut(toAddress, subject, process,helper);
        helper.addAttachment(bookName + ".pdf", file);
    }

    private void mimeOut(String toAddress, String subject, String process, MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {

        helper.setFrom(env.getProperty("fromAddress"), env.getProperty("senderName"));
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);
    }

    private void emailReport(String toAddress, String subject, String date) {

        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setFromAddress(env.getProperty("fromAddress"));
        emailEntity.setToAddress(toAddress);
        emailEntity.setSubject(subject);
        emailEntity.setDate(date);
        emailRepository.save(emailEntity);

    }
}
