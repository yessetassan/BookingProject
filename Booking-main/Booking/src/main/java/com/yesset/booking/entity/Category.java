package com.yesset.booking.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "t_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "name_kz")
    private String nameKz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "parent_id")
    private Long parentCategoryId;

}
