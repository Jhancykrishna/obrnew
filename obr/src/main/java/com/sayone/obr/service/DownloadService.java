package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.DownloadEntity;
import com.sayone.obr.repository.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
