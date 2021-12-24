package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import com.sayone.obr.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;


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

    public void downloadBook(UserDto user, Long bookId) throws MessagingException, IOException {

        BookEntity bookEntity = bookRepository.getById(bookId);

        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

        DownloadEntity downloads = new DownloadEntity();
        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);
        DownloadEntity downloadEntity = downloadRepository.findByUserAndBookId(user.getUserId(), bookId);

        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("book", bookEntity);
        context.setVariable("downloads", downloadEntity);

        String fromAddress = "springobrtest@gmail.com";
        String senderName = "OBR";
        String toAddress = user.getEmail();
        String date = time();

        String bookLink = downloadRepository.findBooksLink(bookId);
        String bookName = downloadRepository.findBookName(bookId);
        System.out.println("book name is"+bookName);
        System.out.println("this is book link "+bookLink);
        String userName = user.getFirstName()+ user.getLastName();
        System.out.println("user name is "+ userName);


        if (optionalDownload.isPresent()) {

            downloadGet = optionalDownload.get();
            long dno = downloadGet.getDno();
            System.out.println("this is dno " + dno);
            long newDno = dno + 1L;


            if (newDno > 3) {

                String process = templateEngine.process("emails/outOfDownloads", context);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                String subject = "Out of Downloads! " + bookName;

                sendEmail3(subject,process,fromAddress,senderName,toAddress,date);

            } else {
                System.out.println("new dno is" + newDno);
                downloadGet.setDno(newDno);
                System.out.println("it's" + downloadGet);

                Long bookIdSend = bookId;

                Long dnoRemain = 3L - downloadGet.getDno();


                String process = templateEngine.process("emails/downloadAgain", context);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                String subject = "Here's your " + bookName + "!";

                sendEmail2(subject,process,fromAddress,senderName,toAddress,bookLink,bookName,bookIdSend,downloadGet,date);
            }
        }
        //1st condition to work(no optional download presents)
        else {

            System.out.println("book id is " + bookId);
            downloads.setBookId(bookId);
            downloads.setDno(1L);
            downloads.setUid(user.getUserId());
            System.out.println("downloading.....");

            //mail test
            Long bookIdSend = bookId;

            String process = templateEngine.process("emails/initialDownload", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            String subject = "Here's your " + bookName + " Enjoy!!";

            sendEmail1(fromAddress,senderName,toAddress,subject,process,bookLink,bookName,downloads,date);
        }


    }

    private void sendEmail1(String fromAddress, String senderName, String toAddress, String subject, String process, String bookLink, String bookName, DownloadEntity downloads, String date) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);
        FileSystemResource file = new FileSystemResource(new File(bookLink));
        helper.addAttachment(bookName + ".pdf", file);
        System.out.println("the file is " + file);
        javaMailSender.send(message);
        downloadRepository.save(downloads);
        System.out.println("1st mail");
        System.out.println("send success");
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setFromAddress(fromAddress);
        emailEntity.setToAddress(toAddress);
        emailEntity.setSubject(subject);
        emailEntity.setDate(date);
        emailRepository.save(emailEntity);
    }

    private void sendEmail2(String subject, String process, String fromAddress, String senderName, String toAddress, String bookLink, String bookName, Long bookIdSend, DownloadEntity downloadGet, String date) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);
        FileSystemResource file = new FileSystemResource(new File(bookLink));
        helper.addAttachment(bookName + ".pdf", file);
        System.out.println("the book id and file is " + file + " " + bookIdSend + " " + bookLink);
        javaMailSender.send(message);
        downloadRepository.save(downloadGet);
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setFromAddress(fromAddress);
        emailEntity.setToAddress(toAddress);
        emailEntity.setSubject(subject);
        emailEntity.setDate(date);
        emailRepository.save(emailEntity);
    }

    private void sendEmail3(String subject, String process, String fromAddress, String senderName, String toAddress, String date) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(process, true);
        javaMailSender.send(message);
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setFromAddress(fromAddress);
        emailEntity.setToAddress(toAddress);
        emailEntity.setSubject(subject);
        emailEntity.setDate(date);
        emailRepository.save(emailEntity);
        throw new UserServiceException(DownloadErrors.DOWNLOAD_LIMIT.getErrorMessage());
    }
}
