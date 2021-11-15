package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
//import com.sayone.obr.entity.PublisherEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Objects;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class DownloadService {

    @Autowired
    DownloadRepository downloadRepository;
    @Autowired(required = false)
    UserEntity userEntity;
    @Autowired(required = false)
    BookEntity bookEntity;
    //    @Autowired
//    PublisherEntity publisherEntity;
    @Autowired
    JavaMailSender javaMailSender;
//    @Autowired(required = false)
//    BookRepository bookRepository;



    public void downloadBook(UserDto user, Long bookId) throws MessagingException, UnsupportedEncodingException {

        DownloadEntity downloads = new DownloadEntity();
        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);


        String fromAddress = "springobrtest@gmail.com";
        String senderName = "OBR";
        String toAddress = user.getEmail();

        String bookLink = downloadRepository.findBooksLink(bookId);
        String bookName = downloadRepository.findBookName(bookId);
        System.out.println("book name is"+bookName);
        System.out.println("this is book link "+bookLink);
        String userName = user.getFirstName()+ user.getLastName();
        System.out.println("user name is "+ userName);


        if (optionalDownload.isPresent()) {
            downloadGet = optionalDownload.get();
//            long dno = downloadRepository.getDownloadCheck(user.getUserId(), bookId);
            long dno = downloadGet.getDno();
            System.out.println("this is dno "+dno);
            long newDno = dno + 1L;
            System.out.println(newDno);
            downloadGet.setDno(newDno);
            System.out.println(downloadGet);

            Long bookIdSend = bookId;

            //System.out.println("getting book id to sent email "+bookIdSend);
            //String userNameSend = userName;
            // System.out.println("getting user name to sent email "+userNameSend);
//            String bookNameSend = bookEntity.getBookName();
//            System.out.println("getting book name to sent mail");

            Long dnoRemain =  10L - downloadGet.getDno() ;

            String body2 = "HI" + userName + " your book "+ bookName +" is successfully downloaded. You've "+ dnoRemain +" downloads left";
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject("here's your "+bookName);
            helper.setText(body2);
            FileSystemResource file = new FileSystemResource(new File(bookLink));
            helper.addAttachment(bookName,file);
            System.out.println("the book id and file is "+file+" "+bookIdSend+ " "+bookLink);
            javaMailSender.send(message);
            downloadRepository.save(downloadGet);
        } else {
            System.out.println("book id is "+bookId);
            System.out.println("book iddd ");
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
            String body1 = "HI" + userName + " thank you for your purchase for "+ bookName +" You can download 10 times";
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject("here's your "+bookName);
            helper.setText("book received");
            FileSystemResource file = new FileSystemResource(new File(bookLink));
            helper.addAttachment(bookName,file);
            System.out.println("the file is "+file);
            javaMailSender.send(message);
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
            return "No such book exist!";
        }
    }

//
//    public void mailBook(String bookId, String userId) throws MessagingException {
//        String bookIdSend = "/home/akhildev/Pictures/" + bookId + ".pdf";
//        String bookNameSend = "fileName";
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo("akhilshadow123@gmail.com");
//        helper.setSubject("test");
//        helper.setText("book received");
//        FileSystemResource file = new FileSystemResource(new File
//                (bookIdSend));
//        helper.addAttachment(bookNameSend,file);
//        javaMailSender.send(message);
//        System.out.println("send success");
//    }
//


//    public void uploadBook(MultipartFile file, String bookId, String userId) throws IOException {
//      Optional<DownloadEntity> optionalUpload = downloadRepository.findUploadArea(bookId,userId);
//      DownloadEntity findUpload = optionalUpload.get();
//      if(optionalUpload.isEmpty()){ throw new IllegalStateException("No book found to upload..");
//      }
//      else {
//          String path ="/home/akhildev/Desktop/newProject/obrnew/obr/BookUploads/" + bookId + ".pdf";
//          file.transferTo(new File(path));
//          findUpload.setBookLink(path);
//          System.out.println("successfully uploaded");
//          downloadRepository.save(findUpload);
//      }
//
//    }
//
//    public void deleteBook(String bookId, String userId) throws IOException {
//        Optional<DownloadEntity> optionalDelete = downloadRepository.findByDeleteArea(bookId, userId);
//        DownloadEntity findDelete = optionalDelete.get();
//        String pathCheck = findDelete.getBookLink();
//        if (Objects.equals(pathCheck, "") || Objects.equals(pathCheck, "deleted..")){
//            System.out.println("No files to delete");
//        }
//        else {
//            Path path = Path.of("/home/akhildev/Desktop/obrnew/obr/BookUploads/" + bookId + ".pdf");
//            Files.delete(path);
//            findDelete.setBookLink("deleted..");
//            System.out.println("deleted successfully");
//            downloadRepository.save(findDelete);
//        }
//    }
//


//    public void findAllDownloads() {
//        List<DownloadEntity> optionalShowDownloads = downloadRepository.findAll();
//    }
}
