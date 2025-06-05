package com.yesset.booking.dto.item;

import com.yesset.booking.entity.Item;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookFullDto {
    private Item item;
    private List<FreeDay> freeDayList;
}
