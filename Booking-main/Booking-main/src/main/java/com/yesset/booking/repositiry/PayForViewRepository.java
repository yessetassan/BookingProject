package com.yesset.booking.repositiry;

import com.yesset.booking.entity.PayForView;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayForViewRepository extends JpaRepository<PayForView, Long> {

    @Query(value = "SELECT * FROM t_pay_for_view " +
            "WHERE item_id = :item_id AND " +
            "status = 'ACTIVE' AND " +
            ":current_date BETWEEN view_from AND view_till", nativeQuery = true)
    Optional<PayForView> findIntersection(
            @Param("current_date") Date current_date,
            @Param("item_id") Long item_id
    );

    Page<PayForView> findAllByItemId(Long itemId, Pageable pageable);
}
