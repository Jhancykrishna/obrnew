package com.sayone.obr.repository;

import com.sayone.obr.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    PublisherEntity findByEmail(String email);

    PublisherEntity findByPublisherId(String publisherId);
}