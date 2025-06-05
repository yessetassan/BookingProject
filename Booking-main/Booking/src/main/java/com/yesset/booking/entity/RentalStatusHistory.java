package com.yesset.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_rental_status_history")
@Data
public class RentalStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private String status;

    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt = LocalDateTime.now();
}
