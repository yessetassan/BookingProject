package com.yesset.booking.service.impl;

import com.yesset.booking.dto.category.CategoryDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Category;
import com.yesset.booking.entity.CategoryAdmin;
import com.yesset.booking.entity.Role;
import com.yesset.booking.entity.User;
import com.yesset.booking.mapper.category.CategoryMapper;
import com.yesset.booking.repositiry.CategoryAdminRepository;
import com.yesset.booking.repositiry.CategoryRepository;
import com.yesset.booking.service.CategoryService;
import com.yesset.booking.util.UserApp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yesset.booking.enums.constains.category.CategoryCons.allCategoryName;
import static com.yesset.booking.util.ErrorStatus.PULL_DATA_ERROR;
import static com.yesset.booking.util.RoleEnum.ROLE_ADMIN;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryAdminRepository categoryAdminRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryAdminRepository categoryAdminRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryAdminRepository = categoryAdminRepository;
    }
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseAnswer<CategoryDto> getAllCategories() {
        ResponseAnswer<CategoryDto> answer = new ResponseAnswer<>();
        answer.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            boolean isAdmin = isAdmin(user);
            List<Category> list = categoryRepository.findAll();
            Category allCategory = null;
            if (!isAdmin) {
                allCategory = findHighest(list);
            }else{
                CategoryAdmin categoryAdmin = categoryAdminRepository.findByUser(user).orElse(null);
                if (categoryAdmin == null) throw new Exception("CategoryAdmin not found");
                allCategory = list
                        .stream()
                        .filter(category ->
                                category.getId().equals(categoryAdmin.getCategory().getId()))
                        .findAny()
                        .orElse(null);

            }
            if (allCategory == null) throw new Exception("Category not found");
            CategoryDto body = CategoryMapper.toDto(allCategory);
            body.setSubcategories(setList(body, list));
            answer.setBody(body);
        } catch (Exception e) {
            answer.setSuccess(Boolean.FALSE);
            answer.setStatus(PULL_DATA_ERROR.toString());
        }
        return answer;
    }

    private boolean isAdmin(User user) {
        if (user == null) return false;
        Role role = user.getRole();
        if (role == null) return false;
        return role.getName().equals(ROLE_ADMIN.toString());
    }

    private List<CategoryDto> setList(CategoryDto body, List<Category> list) {
        Long bodyId = body.getId();
        List<CategoryDto> subcategories = list
                .stream()
                .filter(element ->
                        element != null &&
                                element.getParentCategoryId() != null &&
                                element.getParentCategoryId().equals(bodyId))
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
        body.setSubcategories(subcategories);
        subcategories.forEach(element -> element.setSubcategories(setList(element, list)));
        return subcategories;
    }

    private Category findHighest(List<Category> list) {
        return list
                .stream()
                .filter(element -> element.getName().equals(allCategoryName))
                .findAny()
                .orElse(null);
    }



}
