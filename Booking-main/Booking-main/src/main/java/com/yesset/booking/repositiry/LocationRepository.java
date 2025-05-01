package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Location;
import com.yesset.booking.entity.MicroDistinct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
}