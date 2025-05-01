package com.yesset.booking.endpoint;


import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.user.UserCardRequestDto;
import com.yesset.booking.entity.UserCard;
import com.yesset.booking.service.UserCardService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class UserCardController {

    private final UserCardService userCardService;

    /*
    Получить все карты
     */
    @GetMapping
    public ResponseAnswer<List<UserCard>> getCardsByUserId() {
        return userCardService.getUserCards();
    }

    /*
    Создать карту
     */
    @PostMapping("/create")
    public ResponseAnswer<Object> saveCard(@RequestBody @Validated UserCardRequestDto card) {
        return userCardService.save(card);
    }

    /*
    Удалить карту
     */
    @DeleteMapping("/{id}")
    public ResponseAnswer<Object> deleteCard(@PathVariable Long id) {
        return userCardService.delete(id);
    }
}
