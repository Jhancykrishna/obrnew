package com.sayone.obr.service;

import com.sayone.obr.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getPublisherById(String userId);

    UserDto updatePublisher(String userId, UserDto userDto);

    void deletePublisher(String userId);
}