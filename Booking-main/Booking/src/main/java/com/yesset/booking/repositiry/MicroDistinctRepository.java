package com.yesset.booking.repositiry;

import com.yesset.booking.entity.MicroDistinct;
import com.yesset.booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MicroDistinctRepository extends JpaRepository<MicroDistinct,Long> {
}
