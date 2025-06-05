package com.yesset.booking.service;

import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Filter;

import java.util.List;

public interface FilterService {
    ResponseAnswer<List<FilterDto>> getFiltersByCategoryId(Long categoryId);
}
