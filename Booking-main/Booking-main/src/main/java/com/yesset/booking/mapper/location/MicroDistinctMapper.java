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
        return dto;
    }

    public MicroDistinct toEntity(MicroDistinctDto dto) {
        if (dto == null) {
            return null;
        }

        MicroDistinct entity = new MicroDistinct();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        // parentId can be set outside or by extending the DTO to include parentId if needed
        return entity;
    }

    public List<MicroDistinctDto> toDtoList(List<MicroDistinct> entities) {
        return entities.stream()
                .map(e -> toDto(e, null))  // or fetch children if needed
                .collect(Collectors.toList());
    }

    public List<MicroDistinct> toEntityList(List<MicroDistinctDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
