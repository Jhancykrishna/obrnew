package com.sayone.obr.service.impl;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.entity.UserReviewEntity;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.ReviewRequestModel;
import com.sayone.obr.model.response.ReviewResponseModel;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.repository.UserReviewRepository;
import com.sayone.obr.service.UserReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserReviewServiceImpl implements UserReviewService {

    @Autowired
    UserReviewRepository userReviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;


    @Override
    public ReviewResponseModel createReview(ReviewRequestModel reviewRequestModel, Long Id) {

        BookEntity bookEntity = getBookById(reviewRequestModel.getBookId());
        UserEntity userEntity = getUserByUserId(Id);

        if(bookEntity== null)
            throw new UserServiceException(ErrorMessages.NO_BOOK_ID_FOUND.getErrorMessage());

        if (reviewRequestModel.getRating() > 5)
            throw new UserServiceException(ErrorMessages.INVALID_RATING.getErrorMessage());


        if (userReviewRepository.findByBookIdAndUserId(bookEntity.getBookId(), userEntity.getId()).isPresent())
                throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());


        UserReviewEntity userReviewEntity = new UserReviewEntity();
        BeanUtils.copyProperties(reviewRequestModel, userReviewEntity );

        userReviewEntity.setBookEntity(bookEntity);
        userReviewEntity.setUserEntity(userEntity);



        UserReviewEntity reviewDetails = userReviewRepository.save(userReviewEntity);


        ReviewResponseModel reviewResponseModel = new ReviewResponseModel();
        BeanUtils.copyProperties(reviewDetails , reviewResponseModel);
        reviewResponseModel.setBookName(reviewDetails.getBookEntity().getBookName());
        reviewResponseModel.setUserName(reviewDetails.getUserEntity().getFirstName());
        return reviewResponseModel;

    }


    @Override
    public ReviewResponseModel getReviewsByBookId(Long bookId, Long Id) {
        getBookById(bookId);

        Optional<UserReviewEntity> userReviewEntity= userReviewRepository.findByBookAndUser(bookId, Id);
        if (userReviewEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        ReviewResponseModel reviewResponseModel = new ReviewResponseModel();
        BeanUtils.copyProperties(userReviewEntity.get(), reviewResponseModel);
        reviewResponseModel.setBookName(userReviewEntity.get().getBookEntity().getBookName());
        reviewResponseModel.setUserName(userReviewEntity.get().getUserEntity().getFirstName());
        return reviewResponseModel;

    }

    @Override
    public List<ReviewResponseModel> findReviewsByUser(Long Id) {
        List<UserReviewEntity> reviewEntityList = userReviewRepository.findByUserId(Id);
        if (reviewEntityList.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        List<ReviewResponseModel> reviewResponseModels = new ArrayList<>();
        for (UserReviewEntity userReviewEntity : reviewEntityList) {
            ReviewResponseModel reviewResponseModel = new ReviewResponseModel();
            BeanUtils.copyProperties(userReviewEntity, reviewResponseModel);
            reviewResponseModel.setBookName(userReviewEntity.getBookEntity().getBookName());
            reviewResponseModel.setUserName(userReviewEntity.getUserEntity().getFirstName());
            reviewResponseModels.add(reviewResponseModel);
        }
        return reviewResponseModels;
    }



    @Override
    public ReviewResponseModel updateReview(ReviewRequestModel reviewRequestModel, Long Id) {

        getBookById(reviewRequestModel.getBookId());

        Optional<UserReviewEntity> userReviewEntity = userReviewRepository.findByBookAndUser(reviewRequestModel.getBookId(), Id);
       if (userReviewEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        if (reviewRequestModel.getRating() > 5)
            throw new UserServiceException(ErrorMessages.INVALID_RATING.getErrorMessage());

        userReviewEntity.get().setRating(reviewRequestModel.getRating());
        userReviewEntity.get().setDescription(reviewRequestModel.getDescription());

        UserReviewEntity updatedReview = userReviewRepository.save(userReviewEntity.get());
        ReviewResponseModel reviewResponseModel = new ReviewResponseModel();
        BeanUtils.copyProperties(updatedReview, reviewResponseModel);
        reviewResponseModel.setBookName(updatedReview.getBookEntity().getBookName());
        reviewResponseModel.setUserName(updatedReview.getUserEntity().getFirstName());

        return reviewResponseModel;
    }






    @Override
    public BookEntity getBookById(Long bookId) {
        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isEmpty()) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        return bookEntity.get();
    }

    @Override
    public UserEntity getUserByUserId(Long Id) {
        Optional< UserEntity> userEntity = userRepository.findById(Id);
        if (userEntity.isEmpty()) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        return userEntity.get();
    }

//
//    @Override
//    public UserEntity getUserByUserId(String userId) {
//        Optional< UserEntity> userEntity = userRepository.findByUserId(userId);
////        if (bookEntity.isEmpty()) {
//////            throw new RequestException(ErrorMessages.NO_PRODUCT_FOUND.getErrorMessages());
//////        }
//        return userEntity.get();
//    }

    @Override
    public void deleteReview(Long bookId, Long Id) {
        getBookById(bookId);
        userReviewRepository.deleteReview(bookId, Id);


    }



}
