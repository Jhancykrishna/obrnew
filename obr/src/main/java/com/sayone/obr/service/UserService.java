package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

   //user
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);


    UserDto updateUser( String userId,UserDto user);

    void deleteUserById(String userId);

  //publisher

    UserDto getPublisherById(String userId);

    UserDto updatePublisher(String userId, UserDto userDto);

    String deletePublisher(String userId);

    UserDto getAllPublishersByRole();

    UserDto getAllUsersByRole();

    String deleteUser(String userId);
}