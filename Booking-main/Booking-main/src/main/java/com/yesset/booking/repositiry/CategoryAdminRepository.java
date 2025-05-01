package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Category;
import com.yesset.booking.entity.CategoryAdmin;
import com.yesset.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryAdminRepository extends JpaRepository<CategoryAdmin, Long> {
    Optional<CategoryAdmin> findByUser(User user);
}
