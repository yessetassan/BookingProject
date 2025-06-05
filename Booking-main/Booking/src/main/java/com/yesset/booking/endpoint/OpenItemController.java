package com.yesset.booking.endpoint;

import com.yesset.booking.dto.item.ItemAddDto;
import com.yesset.booking.dto.item.ItemFullViewDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.item.ItemPreViewDto;
import com.yesset.booking.dto.item.ItemRequestDto;
import com.yesset.booking.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/open-api/items")
@RequiredArgsConstructor
public class OpenItemController {

    private final ItemService itemService;

    @PostMapping(value = "/search")
    public ResponseAnswer<Page<ItemPreViewDto>> searchItems(@RequestBody  ItemRequestDto request) {
        return itemService.getFilteredItems(request);
    }

    @GetMapping(value = "/{ItemId}")
    public ResponseAnswer<ItemFullViewDto> getFullData(@PathVariable Long ItemId) {
        return itemService.getFullData(ItemId);
    }


}
