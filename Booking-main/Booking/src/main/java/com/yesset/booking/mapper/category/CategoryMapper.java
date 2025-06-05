package com.yesset.booking.mapper.category;


import com.yesset.booking.dto.category.CategoryDto;
import com.yesset.booking.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setNameKz(category.getNameKz());
        dto.setNameRu(category.getNameRu());
        return dto;
    }
}
