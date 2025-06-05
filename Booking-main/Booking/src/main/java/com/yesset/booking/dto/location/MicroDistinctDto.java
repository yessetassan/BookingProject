package com.yesset.booking.dto.location;

import com.yesset.booking.entity.MicroDistinct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MicroDistinctDto {
    private Long id;
    private String name;
    private String nameKz;
    private String nameRu;
    private List<MicroDistinct> list;
}
