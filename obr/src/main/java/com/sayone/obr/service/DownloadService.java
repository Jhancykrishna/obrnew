package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.repository.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class DownloadService {

    @Autowired
    DownloadRepository downloadRepository;

    public void downloadBook(UserDto user, String bookId) {
        DownloadEntity downloads = new DownloadEntity();
        DownloadEntity downloadGet = new DownloadEntity();
        Optional<DownloadEntity> optionalDownload = downloadRepository.findByUserId(user.getUserId());

        if(optionalDownload.isPresent() ){
            downloadGet = optionalDownload.get();
            long dno = downloadRepository.getDownloadCheck(user.getUserId(), bookId);
            long newDno = dno+1L;
            System.out.println(newDno);
            downloadGet.setDno(newDno);
            System.out.println(downloadGet);
            downloadRepository.save(downloadGet);
        }
        else {

            downloads.setBid(bookId);
            downloads.setDno(1L);
            downloads.setUid(user.getUserId());
            System.out.println("downloading.....");
            downloadRepository.save(downloads);
        }
    }

    public void uploadBook(MultipartFile file, String bookId, String userId) throws IOException {
      Optional<DownloadEntity> optionalUpload = downloadRepository.findUploadArea(bookId,userId);
      DownloadEntity findUpload = optionalUpload.get();
      if(optionalUpload.isEmpty()){ throw new IllegalStateException("No book found to upload..");
      }
      else {
          String path ="/home/akhildev/Desktop/obrnew/obr/BookUploads/" + bookId + ".pdf";
          file.transferTo(new File(path));
          findUpload.setBookLink(path);
          downloadRepository.save(findUpload);
      }
//        DownloadEntity uploads = new DownloadEntity();
//        uploads = downloadRepository.findUploadArea(bookId, userId);

    }
}
