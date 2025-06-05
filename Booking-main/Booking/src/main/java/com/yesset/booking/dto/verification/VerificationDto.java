package com.yesset.booking.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDto {
    private Long id;
    private String fileUrl;
    private Long itemId;
}
