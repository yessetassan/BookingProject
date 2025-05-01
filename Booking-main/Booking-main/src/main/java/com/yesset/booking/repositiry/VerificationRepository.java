package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Item;
import com.yesset.booking.entity.User;
import com.yesset.booking.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByItem(Item item);
}