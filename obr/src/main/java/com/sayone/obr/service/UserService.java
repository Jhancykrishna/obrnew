package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService extends UserDetailsService {

   //user
    UserDto createUser(UserDto user) throws MessagingException;

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);


    UserDto updateUser( String userId,UserDto user);

    void deleteUserById(String userId);

  //publisher

    UserDto getPublisherById(String userId);

    UserDto updatePublisher(String userId, UserDto userDto);

    String deletePublisher(String id);

    List<UserEntity> getAllPublishersByRole();

    List<UserEntity> getAllUsersByRole();

    String deleteUserByAdmin(Long id);

    String deletePublisherByAdmin(Long id);

 List<UserEntity> getAll(int page, int limit);

 String viewProfile(Long id);
}