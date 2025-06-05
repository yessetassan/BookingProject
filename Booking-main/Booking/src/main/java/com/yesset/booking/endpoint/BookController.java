package com.yesset.booking.endpoint;


import com.yesset.booking.dto.item.BookFullDto;
import com.yesset.booking.dto.item.BookRequestDto;
import com.yesset.booking.dto.item.ItemAdminFullViewDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Rental;
import com.yesset.booking.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final ItemService itemService;

    /*
    Данные товара для бронирование
     */
    @GetMapping(value = "/item/{ItemId}")
    public ResponseAnswer<BookFullDto> getFullData(@PathVariable Long ItemId) {
        return itemService.getFullDataToBook(ItemId);
    }


    /*
    Бронировать
     */
    @PostMapping(value = "/item/{ItemId}")
    public ResponseAnswer<Object> book(@PathVariable Long ItemId,
                                       @RequestBody @Validated BookRequestDto requestDto) {
        return itemService.bookNow(ItemId,requestDto);
    }

    /*
    Подтверждать бронирование
     */
    @PostMapping(value = "/{id}")
    public ResponseAnswer<Object> activeBook(@PathVariable Long id) {
        return itemService.activeBook(id);
    }

    /*
    Отменить бронирование
     */
    @PostMapping(value = "/cancel/{id}")
    public ResponseAnswer<Object> cancelBook(@PathVariable Long id) {
        return itemService.cancelBook(id);
    }

    /*
    История бронирование (для хозяина)
     */
    @GetMapping(value = "/item/history/{ItemId}")
    public ResponseAnswer<Page<Rental>> historyForBookingOwner(@PathVariable Long ItemId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return itemService.historyForBookingOwner(ItemId,page,size);
    }

    /*
    История бронирование (для пользователя)
     */
    @GetMapping(value = "/item/history-for-user/{ItemId}")
    public ResponseAnswer<Page<Rental>> historyForBookingUser(@PathVariable Long ItemId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return itemService.historyForBookingUser(ItemId,page,size);
    }


}
