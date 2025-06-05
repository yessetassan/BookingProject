package com.yesset.booking.dto.user;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtAuthenticationResponse {
    private String token;
    private Long userId;
    private String role;
    private String section;
}