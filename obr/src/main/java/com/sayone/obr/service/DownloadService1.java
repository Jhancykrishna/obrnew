package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
//import com.sayone.obr.entity.PublisherEntity;
//import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.entity.EmailEntity;
import com.sayone.obr.exception.DownloadErrors;
//import com.sayone.obr.exception.ErrorMessages;
//import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
//import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
//import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeMessage;
import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Objects;
import java.io.IOException;
//import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class DownloadService1 {

    @Autowired
    Email email;
    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    DownloadEntity downloadEntity;

    public void downloadBook(UserDto user, Long bookId) throws MessagingException, IOException {
        BookEntity bookEntity = bookRepository.getById(bookId);
        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

        DownloadEntity downloads = new DownloadEntity();
        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);

//        if(optionalDownload.isEmpty())throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());

        if (optionalDownload.isPresent()) {
            downloadGet = optionalDownload.get();
            long dno = downloadGet.getDno();
            System.out.println("this is dno " + dno);
            long newDno = dno + 1L;

            if (newDno > 3) {
                email.sendEmail3(user);
                throw new UserServiceException(DownloadErrors.DOWNLOAD_LIMIT.getErrorMessage());
            } else {
                System.out.println("new dno is" + newDno);
                downloadGet.setDno(newDno);
                System.out.println("it's" + downloadGet);
                Long dnoRemain = 3L - downloadEntity.getDno();
                email.sendEmail2(user,bookId,dnoRemain);
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
            email.sendEmail1(user,bookId);
            downloadRepository.save(downloads);
            System.out.println("1st mail");
            System.out.println("send success");
        }
    }



    public String getDownloadNumber1(UserDto user, Long bookId) {

        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);

        if(optionalDownload.isPresent()){
            downloadGet = optionalDownload.get();
            Long dno = downloadGet.getDno();
            String bookName = downloadRepository.findBookName(bookId);
            String downloadNumber = bookName + " is downloaded "+ dno + " times.";
            return downloadNumber;
        }
        else {
//            return "No such book";
            throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());
        }
    }


}

