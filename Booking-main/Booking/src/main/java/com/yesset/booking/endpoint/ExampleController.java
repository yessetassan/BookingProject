package com.yesset.booking.endpoint;
import com.yesset.booking.service.impl.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/open-api/message")
public class ExampleController {
    private final UserService service;

    public ExampleController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String example() {
        return "Booking Project";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

}