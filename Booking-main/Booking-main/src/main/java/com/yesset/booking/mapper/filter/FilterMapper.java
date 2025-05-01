package com.yesset.booking.mapper.filter;

import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.entity.Filter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterMapper {

    public static FilterDto toDto(Filter filter) {
        if (filter == null) {
            return null;
        }
        return new FilterDto(
                filter.getName(),
                filter.getType(),
                filter.getOptions()
        );
    }

    public static List<FilterDto> toDtoList(List<Filter> filters) {
        return filters.stream()
                .map(FilterMapper::toDto)
                .collect(Collectors.toList());
    }

}
