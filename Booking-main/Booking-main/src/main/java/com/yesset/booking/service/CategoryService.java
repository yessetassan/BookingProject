package com.yesset.booking.service;
import com.yesset.booking.dto.category.CategoryDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    ResponseAnswer<CategoryDto> getAllCategories();
}
