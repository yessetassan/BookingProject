package com.yesset.booking.repositiry;
import com.yesset.booking.entity.File;
import com.yesset.booking.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByItem(Item item);
}
