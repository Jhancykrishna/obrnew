package com.sayone.obr.service.impl;

import com.sayone.obr.entity.PublisherEntity;
import com.sayone.obr.repository.PublisherRepository;
import com.sayone.obr.service.PublisherService;
import com.sayone.obr.shared.PublisherUtils;
import com.sayone.obr.shared.dto.PublisherDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PublisherUtils publisherUtils;

    @Override
    public PublisherDto getPublisher(String email) {

        PublisherEntity publisherEntity = publisherRepository.findByEmail(email);

        if(publisherEntity==null) throw new UsernameNotFoundException(email);

        PublisherDto returnValue = new PublisherDto();
        BeanUtils.copyProperties(publisherEntity, returnValue);
        return returnValue;
    }

    @Override
    public PublisherDto createPublisher(PublisherDto publisher) {

        PublisherEntity publisherEntity = new PublisherEntity();
        BeanUtils.copyProperties(publisher, publisherEntity);

//        publisherEntity.setEncryptedPassword("test");
//        publisherEntity.setPublisherId("testPublisherId");

        if(publisherRepository.findByEmail(publisher.getEmail()) != null) throw new RuntimeException("Record already exists");

        String publicPublisherId = publisherUtils.generatePublisherId(30);
        publisherEntity.setPublisherId(publicPublisherId);

        publisherEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(publisher.getPassword()));

        PublisherEntity storedPublisherDetails = publisherRepository.save(publisherEntity);

        PublisherDto returnValue = new PublisherDto();
        BeanUtils.copyProperties(storedPublisherDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deletePublisher(String publisherId) {

        PublisherEntity publisherEntity = publisherRepository.findByPublisherId(publisherId);

        if (publisherEntity == null) {
            throw new IllegalStateException("Record not found");
        }
        publisherRepository.delete(publisherEntity);
    }

    @Override
    public PublisherDto getPublisherByPublisherId(String publisherId) {
        PublisherDto returnValue = new PublisherDto();

        PublisherEntity publisherEntity = publisherRepository.findByPublisherId(publisherId);

        if(publisherEntity == null) throw new UsernameNotFoundException(publisherId);

        BeanUtils.copyProperties(publisherEntity, returnValue);

        return returnValue;
    }

    @Override
    public PublisherDto updatePublisher(String publisherId, PublisherDto publisher) {

        PublisherDto returnValue = new PublisherDto();
        PublisherEntity publisherEntity = publisherRepository.findByPublisherId(publisherId);

        if (publisherEntity == null) throw new UsernameNotFoundException(publisherId);

        publisherEntity.setFirstName(publisher.getFirstName());
        publisherEntity.setLastName(publisher.getLastName());

        PublisherEntity updatedPublisherDetails = publisherRepository.save(publisherEntity);
        BeanUtils.copyProperties(updatedPublisherDetails, returnValue);

        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PublisherEntity publisherEntity = publisherRepository.findByEmail(email);

        if(publisherEntity == null) throw new UsernameNotFoundException(email);

        return new User(publisherEntity.getEmail(), publisherEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
