package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    List<Filter> findByCategoryId(Long categoryId); // Найти фильтры по категории
}
