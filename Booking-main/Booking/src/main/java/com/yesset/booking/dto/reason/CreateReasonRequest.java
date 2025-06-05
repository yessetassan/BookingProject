package com.yesset.booking.dto.reason;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReasonRequest {
    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    private String fileUrl;

    private String fileType;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull(message = "Reason text cannot be null")
    @Size(min = 5, message = "Reason text must be at least 5 characters long")
    private String reasonText;

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;
}
