package com.yesset.booking.endpoint;

import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Review;
import com.yesset.booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @GetMapping
    public ResponseAnswer<Page<Review>> getReviews(
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) String sentiment,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reviewService.getFilteredReviews(itemId, sentiment, page, size);
    }
}
