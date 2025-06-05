package com.yesset.booking.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;


public class ItemResponseDto {
    private int page;
    private int size;
    private int total;
    List<ItemPreViewDto> items;
}
