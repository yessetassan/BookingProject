package com.yesset.booking.endpoint;

import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.review.ReviewCreateDto;
import com.yesset.booking.entity.Review;
import com.yesset.booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewControllerAct {

    private final ReviewService reviewService;

    /*
    Оставить отзыв
     */
    @PostMapping
    public ResponseAnswer<Object> create(@RequestBody @Validated ReviewCreateDto dto) {
        return reviewService.createReview(dto);
    }

    /*
    Редактировать отзыв
     */
    @PostMapping("/update")
    public ResponseAnswer<Object> update(@RequestBody @Validated ReviewCreateDto dto) {
        return reviewService.updateReview(dto);
    }

    /*
    Удалить отзыв
     */
    @DeleteMapping("/{id}")
    public ResponseAnswer<Object> delete(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}
