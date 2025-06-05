package com.yesset.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_translate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Translate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String nameKz;

    @Column(length = 255)
    private String nameRu;
}
