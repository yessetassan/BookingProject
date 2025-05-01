package com.yesset.booking.dto.item;

import com.yesset.booking.entity.File;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemViewPayDto implements Serializable {
    @NotNull(message = "ID не может быть null")
    private Long id;

    @NotBlank(message = "Поле 'from' не может быть пустым")
    private String from;

    @NotNull(message = "Поле 'days' не может быть null")
    @Min(value = 1, message = "Минимальное количество дней — 1")
    private Integer days;

    @NotNull(message = "cartId не может быть null")
    @Positive(message = "cartId должен быть положительным числом")
    private Long cartId;

    @NotNull(message = "price не может быть null")
    @Positive(message = "price должен быть положительным числом")
    private Integer price;
}
