package com.yesset.booking.endpoint;

import com.yesset.booking.dto.category.CategoryDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.service.CategoryService;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/open-api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseAnswer<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
