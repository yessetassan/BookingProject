package com.yesset.booking.repositiry;


import com.yesset.booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByItemId(Long itemId);
    List<Review> findByUserId(Long userId);
}
