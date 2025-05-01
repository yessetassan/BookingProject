package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends  JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    List<Item> findByCategoryId(Long categoryId); // Найти все товары в категории
    List<Item> findByOwnerId(Long ownerId); // Найти все товары конкретного владельца
    List<Item> findByAvailableTrue(); // Найти все доступные товары
}
