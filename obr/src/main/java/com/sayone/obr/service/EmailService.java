package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import com.sayone.obr.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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


//to send emails during downloads:

    String fromAddress = "jhancykrishna1@gmail.com";
    String senderName = "OBR";
    String subject = "Book from OBR";
    Context context = new Context();

    //to send email during first download:
    public String downloadBook(UserDto user, Long bookId) throws MessagingException, IOException {

        BookEntity bookEntity = bookRepository.getById(bookId);
        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

        DownloadEntity downloads = new DownloadEntity();
        downloads.setBookId(bookId);
        downloads.setDno(1L);
        downloads.setUid(user.getUserId());
        DownloadEntity downloadEntity = downloadRepository.findByUserAndBookId(user.getUserId(), bookId);

        String toAddress = user.getEmail();
        context.setVariable("user", user);
        context.setVariable("book", bookEntity);
        context.setVariable("downloads", downloadEntity);

        String process = templateEngine.process("emails/initialDownload", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);
        javaMailSender.send(message);
        downloadRepository.save(downloads);

        EmailEntity emailReport = new EmailEntity();
        emailReport.setFromAddress(fromAddress);
        emailReport.setToAddress(toAddress);
        emailReport.setSubject(subject);
        emailReport.setDate(LocalDate.now());
        emailRepository.save(emailReport);
        return "sent";
    }

//to send email for downloading again
    public void downloadBookAgain(UserDto user, Long bookId) throws MessagingException, UnsupportedEncodingException {
        BookEntity bookEntity = bookRepository.getById(bookId);

        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);

        if (optionalDownload.isPresent()) {
            DownloadEntity downloadGet = new DownloadEntity();
            downloadGet = optionalDownload.get();
            long dno = downloadGet.getDno();
            long newDno = dno + 1L;
            System.out.println(newDno);

            if (newDno <= 3) {
                downloadGet.setDno(newDno);
                Long dnoRemain = 3L - downloadGet.getDno();
                System.out.println(dnoRemain);

                context.setVariable("dnoRemain", dnoRemain);
                context.setVariable("user", user);
                context.setVariable("book", bookEntity);
                String toAddress = user.getEmail();

                String process = templateEngine.process("emails/downloadAgain", context);
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);
                helper.setText(process, true);
                javaMailSender.send(message);
                downloadRepository.save(downloadGet);

            }
        }
    }
//to send email when download limit exceeds.
    public void outOfDownload(UserDto user, Long bookId) throws MessagingException, UnsupportedEncodingException {
        BookEntity bookEntity = bookRepository.getById(bookId);
        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

        Optional<DownloadEntity> optionalDownloads = downloadRepository.findByUserId(user.getUserId(), bookId);

        if (optionalDownloads.isPresent()) {

            DownloadEntity downloadGet = new DownloadEntity();
            downloadGet = optionalDownloads.get();
            long dno = downloadGet.getDno();
            long newDno = dno + 1L;

              if (newDno > 3) {
                context.setVariable("user", user);
                String toAddress = user.getEmail();

                String process = templateEngine.process("emails/outOfDownloads", context);
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);
                helper.setText(process, true);
                javaMailSender.send(message);


            }
        }
    }
}