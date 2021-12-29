package com.sayone.obr.service;
import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;

import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


@Service
public class DownloadService {

    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    Email email;


    public void downloadBook(UserDto user, Long bookId) throws MessagingException, IOException {
        BookEntity bookEntity = bookRepository.getById(bookId);
        if (Objects.equals(user.getUserStatus(), "regular") && Objects.equals(bookEntity.getBookStatus(), "prime"))
            throw new UserServiceException(DownloadErrors.GET_PRIME_ACCOUNT.getErrorMessage());

        DownloadEntity downloads = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId(), bookId);

//        if(optionalDownload.isEmpty())throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());
        DownloadEntity downloadGet = new DownloadEntity();
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
                Long dnoRemain = 3L - downloadGet.getDno();
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
