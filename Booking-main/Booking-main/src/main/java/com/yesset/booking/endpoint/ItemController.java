package com.yesset.booking.endpoint;

import com.yesset.booking.dto.item.*;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.PayForView;
import com.yesset.booking.entity.Payment;
import com.yesset.booking.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /*
    По id подтянуть данные
     */
    @GetMapping(value = "/{ItemId}")
    public ResponseAnswer<ItemAdminFullViewDto> getFullData(@PathVariable Long ItemId) {
        return itemService.getFullDataByAdmin(ItemId);
    }

    /*
    Опубликовать
     */
    @PostMapping(value = "/create")
    public ResponseAnswer<Object> createItemByUser(@RequestBody @Validated ItemAddDto request) {
        return itemService.createItemByUser(request);
    }

    /*
    Удалить товар
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseAnswer<Object> delete(@PathVariable Long id) {
        return itemService.delete(id);
    }

    /*
    Для проверки
     */
    @PostMapping(value = "/checkIn")
    public ResponseAnswer<Page<ItemPreViewDto>> checkIn(@RequestBody  ItemRequestDto request) {
        return itemService.getCheckInItems(request);
    }

    /*
    Успешно прошли проверку
     */
    @PostMapping(value = "/approved")
    public ResponseAnswer<Page<ItemPreViewDto>> approved(@RequestBody  ItemRequestDto request) {
        return itemService.getApprovedItems(request);
    }

    /*
    Успешно пройден товар
     */
    @PostMapping(value = "/approve/{id}")
    public ResponseAnswer<Object> approve(@PathVariable Long id) {
        return itemService.approve(id);
    }

    /*
    Получили отказ
     */
    @PostMapping(value = "/rejected")
    public ResponseAnswer<Page<ItemPreViewDto>> rejected(@RequestBody  ItemRequestDto request) {
        return itemService.getRejectedItems(request);
    }

    /*
    Отказать товару
     */
    @PostMapping(value = "/reject/{id}")
    public ResponseAnswer<Object> reject(@PathVariable Long id) {
        return itemService.reject(id);
    }

}
