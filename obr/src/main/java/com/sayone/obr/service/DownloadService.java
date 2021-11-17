package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.DownloadEntity;
//import com.sayone.obr.entity.PublisherEntity;
//import com.sayone.obr.entity.UserEntity;
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
import org.springframework.stereotype.Service;
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



    public void downloadBook(UserDto user, Long bookId) throws MessagingException, IOException {

        BookEntity bookEntity = bookRepository.getById(bookId);

     if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
         throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

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
//              long dno = downloadRepository.getDownloadCheck(user.getUserId(), bookId);
                long dno = downloadGet.getDno();
                System.out.println("this is dno " + dno);
                long newDno = dno + 1L;
                
                
                  if (newDno > 3) {
                       
                    String bodyOfExceedMessage = "Hi " + userName + " Sorry You've exceeds your download of this book.." +
                            "If you want to continue your downloads and get more memberships please contact Admin." +
                            "Thank You..";
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(fromAddress, senderName);
                    helper.setTo(toAddress);
                    helper.setSubject("Out of Downloads! " + bookName);
                    helper.setText(bodyOfExceedMessage);
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

                    String body2 = "A Book is a Gift You can open again and again. " +
                            "It's good to se you again " + userName + ". Here's your " + bookName + "..It's successfully downloaded. " +
                            "You've " + dnoRemain + " downloads left for this book";
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(fromAddress, senderName);
                    helper.setTo(toAddress);
                    helper.setSubject("Here's your " + bookName + "!");
                    helper.setText(body2);
                    FileSystemResource file = new FileSystemResource(new File(bookLink));
                    helper.addAttachment(bookName + ".pdf", file);
                    System.out.println("the book id and file is " + file + " " + bookIdSend + " " + bookLink);
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
                String body1 = "Hi " + userName + " Welcome to OBR. A Reader lives a thousand lives before he dies....." +
                        " Thank you for your purchase for " + bookName + "from OBR.. Continue purchasing and explore your knowledge through reading." +
                        " You can Only download this book for 3 times." +
                        " Enjoy your reading with OBR.." +
                        " Once again Thank You " + userName;
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject("Here's your " + bookName + " Enjoy!!");
                helper.setText(body1);
                FileSystemResource file = new FileSystemResource(new File(bookLink));
                helper.addAttachment(bookName + ".pdf", file);
                System.out.println("the file is " + file);
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
//            return "No such book";
           throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());
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
