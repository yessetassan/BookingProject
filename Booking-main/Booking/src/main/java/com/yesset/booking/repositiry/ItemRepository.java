package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends  JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    List<Item> findByCategoryId(Long categoryId); // Найти все товары в категории
    List<Item> findByOwnerId(Long ownerId); // Найти все товары конкретного владельца
    List<Item> findByAvailableTrue(); // Найти все доступные товары


    @Query(value = """
        SELECT t_item.id
        FROM t_item
        LEFT JOIN t_recommendations 
            ON t_item.id = t_recommendations.recommended_item_id 
            AND t_recommendations.user_id = :userId
        """, nativeQuery = true)
    List<Long> findAllByUserLog(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM item i WHERE /* ваш фильтр */ ORDER BY CASE WHEN i.id IN :itemIds THEN 0 ELSE 1 END",
            countQuery = "SELECT count(*) FROM item i WHERE /* ваш фильтр */",
            nativeQuery = true)
    Page<Item> findAllWithPriorityItems(@Param("itemIds") List<Long> itemIds, Pageable pageable);

}
