package com.yesset.booking.exception_handler;

import com.yesset.booking.dto.response.ResponseAnswer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import static com.yesset.booking.util.ErrorStatus.VALIDATION_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseAnswer<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        ResponseAnswer<Object> response = ResponseAnswer.builder()
                .success(Boolean.FALSE)
                .body(null)
                .status(VALIDATION_ERROR.toString())
                .reason(ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .findFirst()
                        .orElse("Validation error"))
                .build();

        return ResponseEntity.ok(response);
    }
}
