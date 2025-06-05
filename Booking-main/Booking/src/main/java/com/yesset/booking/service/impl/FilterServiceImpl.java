package com.yesset.booking.service.impl;

import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Filter;
import com.yesset.booking.entity.Translate;
import com.yesset.booking.mapper.filter.FilterMapper;
import com.yesset.booking.repositiry.FilterRepository;
import com.yesset.booking.repositiry.TranslateRepository;
import com.yesset.booking.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yesset.booking.util.ErrorStatus.PULL_DATA_ERROR;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final FilterRepository filterRepository;
    private final FilterMapper filterMapper;
    private final TranslateRepository translateRepository;

    @Override
    public ResponseAnswer<List<FilterDto>> getFiltersByCategoryId(Long categoryId) {
        ResponseAnswer<List<FilterDto>> answer = new ResponseAnswer<>();
        answer.setSuccess(true);
        try {
            List<Translate> translates = translateRepository.findAll();
            List<Filter> filter = filterRepository.findByCategoryId(categoryId);
            answer.setBody(filterMapper.toDtoList(filter,translates));
        }catch (Exception e) {
            answer.setSuccess(false);
            answer.setStatus(PULL_DATA_ERROR.toString());
        }
        return answer;
    }
}
