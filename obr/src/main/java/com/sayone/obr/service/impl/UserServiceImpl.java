package com.sayone.obr.service.impl;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.service.UserService;
import com.sayone.obr.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");


        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getPublisherById(String userId) {

        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updatePublisher(String userId, UserDto userDto) {

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) throw new IllegalStateException("Record not found");

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedPublisherDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedPublisherDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deletePublisher(String userId) {
        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) {
            throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());
        }
        userRepository.delete(userEntity);
    }

    @Override
    public UserDto getAllPublishersByRole() {

        UserEntity userEntity = userRepository.findAllByRole("publisher");
        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getAllUsersByRole() {

        UserEntity userEntity = userRepository.findAllByRole("user");
        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByPublisherId(userId,"user");

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
