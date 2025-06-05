package com.yesset.booking.endpoint;

import com.yesset.booking.dto.item.ItemPreViewDto;
import com.yesset.booking.dto.item.ItemRequestDto;
import com.yesset.booking.dto.reason.CreateReasonRequest;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Reason;
import com.yesset.booking.service.ItemService;
import com.yesset.booking.service.ReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reason")
@RequiredArgsConstructor
public class ReasonController {

    private final ReasonService reasonService;
    private final ItemService itemService;

    /*
    Товары имеющих жалоб
     */
    @PostMapping
    public ResponseAnswer<Page<ItemPreViewDto>> conflicted(@RequestBody ItemRequestDto request) {
        return itemService.getAllConflicted(request);
    }

    // ✅ Создание новой причины
    @PostMapping("/create")
    public ResponseAnswer<Reason> createReason(@RequestBody @Validated CreateReasonRequest reason) {
        return reasonService.createReason(reason);
    }

    // ✅ Получение списка причин с пагинацией и фильтрацией по статусу
    @GetMapping
    public ResponseAnswer<Page<Reason>> getAllReasons(
            @RequestParam(required = false) String status,
            @RequestParam Long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reasonService.getAllReasons(status,itemId, page, size);
    }

    // ✅ Обновление причины
    @PostMapping("/{id}")
    public ResponseAnswer<Object> updateReason(@PathVariable Long id, @RequestBody @Validated CreateReasonRequest updatedReason) {
        return reasonService.updateReason(id, updatedReason);
    }

    // ✅ Удаление причины
    @DeleteMapping("/{id}")
    public ResponseAnswer<Object> deleteReason(@PathVariable Long id) {
        return reasonService.deleteReason(id);
    }


    // ✅ Мой жалобы (клиент написал)
    @GetMapping("/client")
    public ResponseAnswer<Page<Reason>> mineReasons(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reasonService.mineReasons(status, page, size);
    }


    // ✅ Мой жалобы (для владелца)
    @GetMapping("/owner")
    public ResponseAnswer<Page<Reason>> ownerReasons(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return reasonService.ownerReasons(status, page, size);
    }

}
