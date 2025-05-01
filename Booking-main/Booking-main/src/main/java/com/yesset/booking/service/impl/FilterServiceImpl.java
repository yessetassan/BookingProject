package com.yesset.booking.service.impl;

import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Filter;
import com.yesset.booking.mapper.filter.FilterMapper;
import com.yesset.booking.repositiry.FilterRepository;
import com.yesset.booking.service.FilterService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yesset.booking.util.ErrorStatus.PULL_DATA_ERROR;

@Service
public class FilterServiceImpl implements FilterService {
    public FilterServiceImpl(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    private final FilterRepository filterRepository;

    @Override
    public ResponseAnswer<List<FilterDto>> getFiltersByCategoryId(Long categoryId) {
        ResponseAnswer<List<FilterDto>> answer = new ResponseAnswer<>();
        answer.setSuccess(true);
        try {
            List<Filter> filter = filterRepository.findByCategoryId(categoryId);
            answer.setBody(FilterMapper.toDtoList(filter));
        }catch (Exception e) {
            answer.setSuccess(false);
            answer.setStatus(PULL_DATA_ERROR.toString());
        }
        return answer;
    }
}
