package com.sayone.obr.service;

import com.sayone.obr.entity.PublisherEntity;
import com.sayone.obr.shared.dto.PublisherDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PublisherService extends UserDetailsService {

    PublisherDto getPublisher(String email);

    PublisherDto createPublisher(PublisherDto publisher);

    void deletePublisher(String publisherId);

    PublisherDto getPublisherByPublisherId(String publisherId);

    PublisherDto updatePublisher(String publisherId, PublisherDto publisher);

}
