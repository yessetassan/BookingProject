package com.yesset.booking.dto.category;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto{
    private Long id;
    private String name;
    private List<CategoryDto> subcategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryDto> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<CategoryDto> subcategories) {
        this.subcategories = subcategories;
    }
}
