package com.yesset.booking.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto{
    private Long id;
    private String name;
    private String nameKz;
    private String nameRu;
    private List<CategoryDto> subcategories;
}
