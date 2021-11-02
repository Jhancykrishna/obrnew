package com.sayone.obr.ui.controller;


import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.service.DownloadService;
import com.sayone.obr.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class DownloadController {

    @Autowired
    DownloadService downloadService;
    
    @Autowired
    UserService userService;

    @PostMapping("/download/{bid}")
    public String createDownload(@PathVariable(value = "bid") String bookId){
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);
        downloadService.downloadBook(user,bookId);
        System.out.println("haI "+user.getUserId());

        return user.getUserId()+" "+user.getEmail() + "downloaded";
    }
}
