package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Translate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslateRepository extends JpaRepository<Translate, Long> {
    Optional<Translate> findByName(String name);
}
