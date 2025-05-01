package com.yesset.booking.specification.review;

import com.yesset.booking.entity.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> hasItemId(Long itemId) {
        return (root, query, cb) -> itemId == null ? null : cb.equal(root.get("item").get("id"), itemId);
    }
    public static Specification<Review> hasUserId(Long userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }
    public static Specification<Review> hasSentiment(String sentiment) {
        return (root, query, cb) -> {
            if (sentiment == null) return null;
            if (sentiment.equalsIgnoreCase("positive")) {
                return cb.between(root.get("rating"), 4, 5);
            } else if (sentiment.equalsIgnoreCase("negative")) {
                return cb.between(root.get("rating"), 1, 3);
            }
            return null;
        };
    }
}
