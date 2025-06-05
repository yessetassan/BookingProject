package com.yesset.booking.repositiry;

import com.yesset.booking.entity.Reason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Long> {
    Page<Reason> findAllByItemIdAndIsActive(Long itemId,Boolean isActive, Pageable pageable);

    Page<Reason> findAllByStatusAndIsActiveAndItemId(String status,Boolean isActive, Long itemId, Pageable pageable);

    Page<Reason> findAllByUserId(Long userId, Pageable pageable);

    Page<Reason> findAllByStatusAndUserId(String status, Long userId, Pageable pageable);
    @Query(value = """
    SELECT tr.*
    FROM t_reason tr
    JOIN t_item ti ON ti.id = tr.item_id
    WHERE ti.owner_id = :ownerId and
    tr.is_active = true
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM t_reason tr
    JOIN t_item ti ON ti.id = tr.item_id
    WHERE ti.owner_id = :ownerId and
    tr.is_active = true
    """,
            nativeQuery = true)
    Page<Reason> findPagedReasonsByItemOwner(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query(value = """
    SELECT tr.*
    FROM t_reason tr
    JOIN t_item ti ON ti.id = tr.item_id
    WHERE tr.status = :status
      AND ti.owner_id = :ownerId and
    tr.is_active = true
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM t_reason tr
    JOIN t_item ti ON ti.id = tr.item_id
    WHERE tr.status = :status
      AND ti.owner_id = :ownerId and
    tr.is_active = true
    """,
            nativeQuery = true)
    Page<Reason> findByStatusAndItemOwner(@Param("status") String status,
                                          @Param("ownerId") Long ownerId,
                                          Pageable pageable);

}
