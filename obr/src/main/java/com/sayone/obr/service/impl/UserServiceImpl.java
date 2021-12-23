package com.sayone.obr.service.impl;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.AdminErrorMessages;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.repository.UserPaginationRepository;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.service.UserService;
import com.sayone.obr.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPaginationRepository userPaginationRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    //user
    @Override
    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) !=null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity );

//        userEntity.setUserId("testUser");
//        userEntity.setEncryptedPassword("test");

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

        UserEntity userEntity= userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId)
    {
        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.getUserByUserId(userId);
        if(userEntity== null) throw new UsernameNotFoundException(userId);
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;

    }



    @Override
    public UserDto updateUser( String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);
        if(userEntity.isEmpty()) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.get().setFirstName(user.getFirstName());
        userEntity.get().setLastName(user.getLastName());
        userEntity.get().setPhoneNumber(user.getPhoneNumber());
        userEntity.get().setUserStatus(user.getUserStatus());
        UserEntity updatedUserDetails = userRepository.save(userEntity.get());
        BeanUtils.copyProperties(updatedUserDetails, returnValue);
        return returnValue;
    }

    @Override
    public void deleteUserById(String userId){
        userRepository.deleteByUserId(userId);
    }

    //publisher,admin

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

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        if (Objects.equals(userDto.getFirstName(), "")) throw new UserServiceException(PublisherErrorMessages.MISSING_FIRST_NAME.getPublisherErrorMessages());
        userEntity.setFirstName(userDto.getFirstName());
        if (Objects.equals(userDto.getLastName(), "")) throw new UserServiceException(PublisherErrorMessages.MISSING_LAST_NAME.getPublisherErrorMessages());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedPublisherDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedPublisherDetails, returnValue);

        return returnValue;
    }

    @Override
    public String deletePublisher(String userId) throws UserServiceException {
        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) {
            throw new UserServiceException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());
        }
        userRepository.delete(userEntity);

        return PublisherErrorMessages.DELETED_ACCOUNT.getPublisherErrorMessages();
    }

    @Override
    public List<UserEntity> getAllPublishersByRole() {

        List<UserEntity> userEntity = userRepository.findAllByRole("publisher");
        if (userEntity.isEmpty()) throw new UserServiceException(AdminErrorMessages.NO_PUBLISHERS_EXIST.getAdminErrorMessages());

//        UserDto returnValue = new UserDto();
//        BeanUtils.copyProperties(userEntity, returnValue);

        return userEntity;
    }

    @Override
    public List<UserEntity> getAllUsersByRole() {

        List<UserEntity> userEntity = userRepository.findAllByRole("user");
        if (userEntity == null) throw new IllegalStateException(AdminErrorMessages.NO_USERS_EXIST.getAdminErrorMessages());

//        UserRestModel returnValue = new UserDto();
//        BeanUtils.copyProperties(userRestModels, returnValue);

        return userEntity;
    }

    @Override
    public String deletePublisherByAdmin(Long id) throws UserServiceException {
        UserEntity userEntity = userRepository.findById(id,"publisher");

        if (userEntity == null) {
            throw new UserServiceException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());
        }
        userRepository.delete(userEntity);

        return PublisherErrorMessages.DELETED_ACCOUNT.getPublisherErrorMessages();
    }

    @Override
    public List<UserEntity> getAll(int page, int limit) {

        List<UserEntity> returnValue = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userPaginationRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for(UserEntity userEntity: users) {
            UserEntity user = new UserEntity();
            user.setFirstName(userEntity.getFirstName());
            user.setLastName(userEntity.getLastName());
            user.setEmail(userEntity.getEmail());
            user.setAddress(userEntity.getAddress());
            user.setUserStatus(userEntity.getUserStatus());
            user.setRole(userEntity.getRole());

            BeanUtils.copyProperties(userEntity,user);
            returnValue.add(user);

        }
        return returnValue;
    }

    @Override
    public String viewProfile(Long id) {

        List<Object[]> objs = userRepository.findAllBooks(id);

        Object[] obj = objs.get(0);

        String userFirstName = String.valueOf(obj[0]);
        String userEmail = String.valueOf(obj[1]);
        String userPhoneNumber = String.valueOf(obj[2]);
        String userAddress = String.valueOf(obj[3]);
        String bookName = String.valueOf(obj[4]);
        String authorName = String.valueOf(obj[5]);
        String yearOfPublication = String.valueOf(obj[6]);

        return  "userFirstName=" + userFirstName + '\n' +
                " userEmail=" + userEmail + '\n' +
                " userPhoneNumber=" + userPhoneNumber + '\n' +
                " userAddress=" + userAddress + '\n' +
                " bookName=" + bookName + '\n' +
                " authorName=" + authorName + '\n' +
                " yearOfPublication=" + yearOfPublication +'\n';
    }

    @Override
    public String deleteUserByAdmin(Long id) {

        UserEntity userEntity = userRepository.findById(id,"user");

        if (userEntity == null) throw new UserServiceException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        userRepository.delete(userEntity);

        return PublisherErrorMessages.DELETED_ACCOUNT.getPublisherErrorMessages();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}