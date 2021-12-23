package com.sayone.obr.service;
import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.User;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import jdk.swing.interop.SwingInterOpUtils;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    DownloadRepository downloadRepository;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    public String sendMail(User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", user);

        String process = templateEngine.process("Welcome", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome " + user.getName());
        helper.setText(process, true);
        helper.setTo(user.getEmail());
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

//        if(optionalDownload.isEmpty())throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());

        String fromAddress = "jhancykrishna1@gmail.com";
        String senderName = "OBR";
        String toAddress = user.getEmail();

        String bookLink = downloadRepository.findBooksLink(bookId);
        String bookName = downloadRepository.findBookName(bookId);
        String userName = user.getFirstName() + user.getLastName();
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("bookName", bookName);
        context.setVariable("bookLink", bookLink);
        context.setVariable("userName", userName);
//
        String process = templateEngine.process("download", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome " + user.getFirstName());
        helper.setText(process, true);
        helper.setTo(user.getEmail());
        javaMailSender.send(mimeMessage);



        if (optionalDownload.isPresent()) {

            downloadGet = optionalDownload.get();
//              long dno = downloadRepository.getDownloadCheck(user.getUserId(), bookId);
            long dno = downloadGet.getDno();
            System.out.println("this is dno " + dno);
            long newDno = dno + 1L;


            if (newDno > 3) {

                process = templateEngine.process("warning", context);
                MimeMessage message = javaMailSender.createMimeMessage();
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject("Out of Downloads! " + bookName);
                helper.setText(process, true);
                javaMailSender.send(message);
                throw new UserServiceException(DownloadErrors.DOWNLOAD_LIMIT.getErrorMessage());

            } else {
                System.out.println("new dno is" + newDno);
                downloadGet.setDno(newDno);
                System.out.println("it's" + downloadGet);

                Long bookIdSend = bookId;

                //System.out.println("getting book id to sent email "+bookIdSend);
                //String userNameSend = userName;
                // System.out.println("getting user name to sent email "+userNameSend);
//                  String bookNameSend = bookEntity.getBookName();
//                  System.out.println("getting book name to sent mail");

                Long dnoRemain = 3L - downloadGet.getDno();
                context.setVariable("DownloadNo", dnoRemain);

                String process2 = templateEngine.process("RemainingDwd", context);
                MimeMessage message = javaMailSender.createMimeMessage();
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject("Your " + bookName + " is here!");
                helper.setText(process2,true);
//                FileSystemResource file = new FileSystemResource(new File(bookLink));
//                helper.addAttachment(bookName + ".pdf", file);
//                System.out.println("the book id and file is " + file + " " + bookIdSend + " " + bookLink);
                javaMailSender.send(message);
                downloadRepository.save(downloadGet);
            }
        }
        //1st condition to work(no optional download presents)
        else {

            System.out.println("book id is " + bookId);
//              System.out.println("book iddd ");
            downloads.setBookId(bookId);
            downloads.setDno(1L);
            downloads.setUid(user.getUserId());
            System.out.println("downloading.....");

            //mail test
            Long bookIdSend = bookId;
            // System.out.println("getting book id to sent email "+bookIdSend);
            //String userNameSend = userName;
            // System.out.println("getting user name to sent email "+userNameSend);
//            String bookNameSend = bookEntity.getBookName();
//            System.out.println("getting book name to sent mail");

            //Long dnoRemain = - 10L - downloadGet.getDno() ;
            String process1 = templateEngine.process("downloadMsg", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject("Here's your " + bookName + " Enjoy!!");
            helper.setText(process1, true);
//            FileSystemResource file = new FileSystemResource(new File(bookLink));
//            helper.addAttachment(bookName + ".pdf", file);
//            System.out.println("the file is " + file);
            javaMailSender.send(message);
            downloadRepository.save(downloads);
            System.out.println("1st mail");
            System.out.println("send success");
        }


    }


//    public String getDownloadNumber1(UserDto user, Long bookId) {
//
//        DownloadEntity downloadGet = new DownloadEntity();
//        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);
//
//        if(optionalDownload.isPresent()){
//            downloadGet = optionalDownload.get();
//            Long dno = downloadGet.getDno();
//            String bookName = downloadRepository.findBookName(bookId);
//            String downloadNumber = bookName + " is downloaded "+ dno + " times.";
//            return downloadNumber;
//        }
//        else {
////            return "No such book";
//            throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());
//        }
//    }


}
