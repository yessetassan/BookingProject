package com.yesset.booking.mapper.favourite;

import com.yesset.booking.dto.favourite.FavouriteDto;
import com.yesset.booking.entity.Favourite;
import com.yesset.booking.entity.File;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteMapper {

    public FavouriteDto toDto(Favourite favourite, List<File> files) {
        if (favourite == null) {
            return null;
        }

        FavouriteDto dto = new FavouriteDto();
        dto.setId(favourite.getId().longValue()); // Convert Integer to Long
        dto.setUser(favourite.getUser());
        dto.setItem(favourite.getItem());
        dto.setFiles(files); // Assuming you're fetching associated files elsewhere
        return dto;
    }

    public Favourite toEntity(FavouriteDto dto) {
        if (dto == null) {
            return null;
        }

        Favourite favourite = new Favourite();
        // If dto.getId() is not null, cast to Integer
        if (dto.getId() != null) {
            favourite.setId(dto.getId().intValue());
        }
        favourite.setUser(dto.getUser());
        favourite.setItem(dto.getItem());
        // Files are not part of the entity, so we skip them
        return favourite;
    }
}
