package com.yesset.booking.service;


import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.review.ReviewCreateDto;
import com.yesset.booking.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    ResponseAnswer<Object> createReview(ReviewCreateDto dto);
    ResponseAnswer<Object> updateReview(ReviewCreateDto dto);
    ResponseAnswer<Object> deleteReview(Long id);
    ResponseAnswer<Page<Review>> getFilteredReviews(Long itemId, String sentiment, int page, int size);
}