package com.yesset.booking.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String firstName;

    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String lastName;

    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phone;
}