package com.yesset.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_filter")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(nullable = false, length = 50)
    private String type; // range | multi-select

    @Column(columnDefinition = "jsonb")
    private String options;
}
