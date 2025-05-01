package com.yesset.booking.dto.favourite;

import com.yesset.booking.entity.File;
import com.yesset.booking.entity.Item;
import com.yesset.booking.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDto {
    private Long id;
    private User user;
    private Item item;
    private List<File> files;
}
