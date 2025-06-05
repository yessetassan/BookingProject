package com.yesset.booking.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDto {
    private Long id;
    @Min(value = 1, message = "Рейтинг должен быть не меньше 1")
    @Max(value = 5, message = "Рейтинг должен быть не больше 5")
    private int rating;

    @NotNull(message = "ID товара не должен быть пустым")
    private Long itemId;

    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 1000, message = "Комментарий не должен превышать 1000 символов")
    private String comment;
}
