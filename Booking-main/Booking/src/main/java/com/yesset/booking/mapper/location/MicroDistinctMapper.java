package com.yesset.booking.mapper.location;

import com.yesset.booking.dto.location.MicroDistinctDto;
import com.yesset.booking.entity.MicroDistinct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MicroDistinctMapper {

    public static MicroDistinctDto toDto(MicroDistinct entity, List<MicroDistinct> children) {
        if (entity == null) {
            return null;
        }

        MicroDistinctDto dto = new MicroDistinctDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setList(children);
        dto.setNameKz(entity.getNameKz());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }
}
