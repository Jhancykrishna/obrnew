package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
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

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    Context context = new Context();

    public void sendMail(UserDetailsRequestModel userDetails) throws MessagingException {

        context.setVariable("user", userDetails);
        String process = templateEngine.process("emails/welcome", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome " + userDetails.getFirstName() + " " + userDetails.getLastName());
        helper.setText(process, true);
        helper.setTo(userDetails.getEmail());
        javaMailSender.send(mimeMessage);
    }

    void sendEmailInitial(Long bookId, UserDto user, String bookName, String bookLink, String toAddress, String date, FileSystemResource file, DownloadEntity downloadEntity, BookEntity bookEntity, long dno) throws MessagingException, UnsupportedEncodingException {

        context.setVariable("user", user);
        context.setVariable("book", bookEntity);
        context.setVariable("downloads", downloadEntity);
        String process = templateEngine.process("emails/initialDownload", context);
        String subject = String.format(env.getProperty("subject.initial"),bookName);
        MimeMessage message = mime(toAddress, subject, process, bookName, file, user, bookId, dno);
        System.out.println("the file is " + file);
        System.out.println("1st mail");
        javaMailSender.send(message);
        System.out.println("send success");
        emailReport(toAddress,subject,date);
    }

    void sendEmailAgain(String bookLink, String bookName, Long bookId, UserDto user, String toAddress, String date, FileSystemResource file, DownloadEntity downloadEntity, BookEntity bookEntity, long newDno) throws MessagingException, UnsupportedEncodingException {

        context.setVariable("user", user);
        context.setVariable("book", bookEntity);
        context.setVariable("downloads", downloadEntity);
        String process = templateEngine.process("emails/downloadAgain", context);
        String subject = String.format(env.getProperty("subject.again"),bookName);
        MimeMessage message = mime(toAddress, subject, process, bookName, file, user, bookId, newDno);
        System.out.println("the book id and file is " + file + " " + bookId + " " + bookLink);
        javaMailSender.send(message);
        emailReport(toAddress,subject,date);
    }

    void sendEmailOut(Long bookId, UserDto user, String bookName, String bookLink, String toAddress, String date, FileSystemResource file, DownloadEntity downloadEntity, BookEntity bookEntity, long newDno) throws MessagingException, UnsupportedEncodingException {

        context.setVariable("user", user);
        String process = templateEngine.process("emails/outOfDownloads", context);
        String subject = String.format(env.getProperty("subject.out"),bookName);
        MimeMessage message = mime(toAddress, subject, process, bookName, file, user, bookId, newDno);
        javaMailSender.send(message);
        emailReport(toAddress,subject,date);
        throw new UserServiceException(DownloadErrors.DOWNLOAD_LIMIT.getErrorMessage());
    }

    private MimeMessage mime(String toAddress, String subject, String process, String bookName, FileSystemResource file, UserDto user,Long bookId, long newDno) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(env.getProperty("fromAddress"), env.getProperty("senderName"));
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);

        if (newDno > 3) {
                return message;
            }
        helper.addAttachment(bookName + ".pdf", file);
        return message;
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
