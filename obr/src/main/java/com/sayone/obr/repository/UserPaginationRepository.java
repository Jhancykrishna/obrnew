package com.sayone.obr.repository;

import com.sayone.obr.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPaginationRepository extends PagingAndSortingRepository<UserEntity, Long> {


}
