package com.yesset.booking.dto.item;

import com.yesset.booking.entity.Item;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookRequestDto {
    @NotBlank(message = "Поле 'from' не должно быть пустым")
    private String from;

    @NotBlank(message = "Поле 'to' не должно быть пустым")
    private String to;

    @NotNull(message = "Поле 'totalPrice' не должно быть null")
    @Positive(message = "Поле 'totalPrice' должно быть положительным числом")
    private Double totalPrice;

    @NotNull(message = "Поле 'cartId' не должно быть null")
    @Positive(message = "Поле 'cartId' должно быть положительным числом")
    private Long cartId;
}
