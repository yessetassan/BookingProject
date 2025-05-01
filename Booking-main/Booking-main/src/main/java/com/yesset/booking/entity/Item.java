package com.yesset.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private String attributes; // JSONB для хранения характеристик

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted_by_admin", nullable = false)
    private Boolean deleted;

    @Column(name = "is_approved", nullable = false)
    private Boolean approved;

    @Column(name = "is_rejected", nullable = false)
    private Boolean rejected;

    @Column(name = "status", nullable = false)
    private String status;
}

