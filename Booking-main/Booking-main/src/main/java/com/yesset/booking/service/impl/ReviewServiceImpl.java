package com.yesset.booking.service.impl;


import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.review.ReviewCreateDto;
import com.yesset.booking.entity.Item;
import com.yesset.booking.entity.Review;
import com.yesset.booking.entity.User;
import com.yesset.booking.repositiry.ItemRepository;
import com.yesset.booking.repositiry.ReviewRepository;
import com.yesset.booking.service.ReviewService;
import com.yesset.booking.specification.review.ReviewSpecification;
import com.yesset.booking.util.UserApp;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import static com.yesset.booking.util.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;

    @Override
    public ResponseAnswer<Object> createReview(ReviewCreateDto dto) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Item item = getItem(dto);
            User user = UserApp.getCurrentUser();
            Review review = createReviewNew(item,user,dto);
            reviewRepository.save(review);
            response.setBody(review);
        }catch (Exception e){
            response.setSuccess(false);
            response.setStatus(REVIEW_CREATION_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> updateReview(ReviewCreateDto dto) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Review review = reviewRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            review.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            response.setBody(review);
        }catch (Exception e){
            response.setSuccess(false);
            response.setStatus(REVIEW_MODIFICATION_ERROR.toString());
        }
        return response;
    }

    private Review createReviewNew(Item item, User user, ReviewCreateDto dto) {
        Review review = new Review();
        review.setItem(item);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setCreatedAt(LocalDateTime.now());
        review.setComment(dto.getComment());
        return review;
    }

    private Item getItem(ReviewCreateDto dto) {
        Optional<Item> itemOptional = itemRepository.findById(dto.getItemId());
        if (itemOptional.isEmpty()) throw new RuntimeException("Item not found");
        return itemOptional.get();
    }

    @Override
    public ResponseAnswer<Object> deleteReview(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            reviewRepository.deleteById(id);
        }catch (Exception e){
            response.setSuccess(false);
            response.setStatus(REVIEW_DELETION_ERROR.toString());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<Review>> getFilteredReviews(Long itemId, String sentiment, int page, int size) {
        ResponseAnswer<Page<Review>> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            log.info("itemId: {}, sentiment: {}", itemId, sentiment);
            Specification<Review> spec = Specification
                    .where(ReviewSpecification.hasItemId(itemId))
                    .and(ReviewSpecification.hasSentiment(sentiment));
            Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
            response.setBody(reviewRepository.findAll(spec, pageable));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(PULL_DATA_ERROR.toString());

        }
        return response;
    }
}
