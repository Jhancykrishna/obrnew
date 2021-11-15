package com.sayone.obr.service;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.model.request.ReviewRequestModel;
import com.sayone.obr.model.response.ReviewResponseModel;

import java.util.List;

public interface UserReviewService {


    ReviewResponseModel createReview(ReviewRequestModel reviewRequestModel, Long Id);


    ReviewResponseModel getReviewsByBookId(Long bookId,Long Id);

    List<ReviewResponseModel> findReviewsByUser(Long Id);

    ReviewResponseModel updateReview(ReviewRequestModel reviewRequestModel, Long Id);

    BookEntity getBookById(Long bookId);

    UserEntity getUserByUserId(Long Id);


    void deleteReview(Long bookId, Long Id);
}
