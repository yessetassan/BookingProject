package com.yesset.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseAnswer<T> {
    private boolean success;
    private T body;
    private String status;
    private String reason;
    private String reasonRu;
}
