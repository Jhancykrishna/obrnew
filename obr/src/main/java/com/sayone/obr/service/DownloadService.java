package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

@Service
public class DownloadService {

    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    BookRepository bookRepository;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    EmailService emailService;


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

        String toAddress = user.getEmail();
        String date = String.valueOf(Calendar.getInstance().getTime());

        String bookLink = downloadRepository.findBooksLink(bookId);
        String bookName = downloadRepository.findBookName(bookId);
        System.out.println("book name is" + bookName);
        System.out.println("this is book link " + bookLink);
        String userName = user.getFirstName() + user.getLastName();
        System.out.println("user name is " + userName);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        FileSystemResource file = new FileSystemResource(new File(bookLink));


        if (optionalDownload.isPresent()) {

            downloadGet = optionalDownload.get();
            long dno = downloadGet.getDno();
            System.out.println("this is dno " + dno);
            long newDno = dno + 1L;


            if (newDno > 3) {

                String process = templateEngine.process("emails/outOfDownloads", context);

                String subject = "Out of Downloads! " + bookName;

                emailService.sendEmail3(subject, process, toAddress, date, message, helper);

            } else {
                System.out.println("new dno is" + newDno);
                downloadGet.setDno(newDno);
                System.out.println("it's" + downloadGet);

                Long bookIdSend = bookId;

                Long dnoRemain = 3L - downloadGet.getDno();


                String process = templateEngine.process("emails/downloadAgain", context);
                String subject = "Here's your " + bookName + "!";

                emailService.sendEmail2(subject, process, toAddress, bookLink, bookName, bookIdSend, date, message, file, helper);

                downloadRepository.save(downloadGet);
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
            String subject = "Here's your " + bookName + " Enjoy!!";

            emailService.sendEmail1(toAddress, subject, process, bookLink, bookName, date, message, file, helper);

            downloadRepository.save(downloads);
        }

    }


    public String getDownloadNumber1(UserDto user, Long bookId) {

        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);

        if (optionalDownload.isPresent()) {
            downloadGet = optionalDownload.get();
            Long dno = downloadGet.getDno();
            String bookName = downloadRepository.findBookName(bookId);
            String downloadNumber = bookName + " is downloaded " + dno + " times.";
            return downloadNumber;
        } else {
//            return "No such book";
            throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());
        }
    }

}