package com.yesset.booking.dto.item;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FreeDay {
    private String format;
    private String month;
    private String dayOfMonth;
    private String dayOfWeek;
    private boolean isFree;
}
