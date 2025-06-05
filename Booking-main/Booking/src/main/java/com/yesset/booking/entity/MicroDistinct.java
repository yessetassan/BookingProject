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

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "parent_id")
    private Long parentId;
}
