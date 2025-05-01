package com.yesset.booking.dto.item;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemAddDto {

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Название обязательно")
    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @NotNull(message = "Описание обязательно")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @NotNull(message = "Цена обязательна")
    @Min(value = 1, message = "Цена должна быть больше 0")
    private Integer pricePerDay;

    @NotNull(message = "Доступность обязательна")
    private Boolean available;

    @NotNull(message = "Количество дней обязательно")
    @Min(value = 0, message = "Минимальное количество дней — 0")
    private Integer days;

    @NotNull(message = "Не должно быть пустым microDistinctId")
    @Min(value = 0, message = "Минимальное количество microDistinctId — 0")
    private Long microDistinctId;

    @NotNull(message = "Атрибуты обязательны")
    @NotBlank(message = "Атрибуты не должны быть пустыми")
    private String attributes;

    @NotNull(message = "Полный адрес обязательна")
    @NotBlank(message = "Полный адрес не должна быть пустой")
    private String fullAddress;

    @NotNull(message = "verificationDocument обязательна")
    @NotBlank(message = "verificationDocument не должна быть пустой")
    private String verificationDocument;

    @NotNull(message = "Список ссылок обязателен")
    @NotEmpty(message = "Необходимо добавить хотя бы одну ссылку")
    private List<@NotBlank(message = "Ссылка не должна быть пустой") String> urls;
}
