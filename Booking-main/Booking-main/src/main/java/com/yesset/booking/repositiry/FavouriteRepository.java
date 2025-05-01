package com.yesset.booking.repositiry;


import com.yesset.booking.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {
    List<Favourite> findByUserId(long userId);
    List<Favourite> findByUserIdAndItemId(long userId,long itemId);

}
