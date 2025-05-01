package com.yesset.booking.repositiry;


import com.yesset.booking.endpoint.OpenItemController;
import com.yesset.booking.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query(value = "select * from t_rental " +
            "where item_id = :itemId and " +
            "status in ('APPROVED','RETURNED_DAMAGED','COMPLETED') and " +
            ":bookDate between start_datetime::date and end_datetime::date",
            nativeQuery = true)
    Optional<Rental> findByItemIdAndDate(Long itemId, LocalDate bookDate);

    @Query(value = "select * from t_rental " +
            "where item_id = :itemId and " +
            "status in ('APPROVED','RETURNED_DAMAGED','COMPLETED') and " +
            ":start_book < start_datetime::date and :end_book > start_datetime::date",
            nativeQuery = true)
    Optional<Rental> searchForExistingBook(Long itemId, LocalDate start_book, LocalDate end_book);



    @Query("SELECT r FROM Rental r WHERE r.item.id = :itemId and r.owner.id = :owner_id")
    Page<Rental> findByItemIdAndOwner(@Param("itemId") Long itemId,@Param("owner_id") Long owner_id, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.item.id = :itemId and r.renter.id = :user_id")
    Page<Rental> findByItemIdAndUser(@Param("itemId") Long itemId,@Param("user_id") Long user_id, Pageable pageable);
}

