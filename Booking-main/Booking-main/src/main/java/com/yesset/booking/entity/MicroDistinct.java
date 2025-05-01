package com.yesset.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "t_microdistrict")
@AllArgsConstructor
@NoArgsConstructor
public class MicroDistinct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;
}
