package com.yesset.booking.endpoint;

import com.yesset.booking.dto.item.ItemViewPayDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.PayForView;
import com.yesset.booking.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class RentalController {

    private final ItemService itemService;


    /*
   Оплатить за аренду товара
    */
    @PostMapping(value = "/payment-for-view")
    public ResponseAnswer<Object> paymentForView(@RequestBody ItemViewPayDto request) {
        return itemService.payForView(request);
    }


    /*
    Отменить за аренду товара
     */
    @PostMapping(value = "/cancel-payment-for-view/{paymentId}")
    public ResponseAnswer<Object> cancelPaymentForView(@PathVariable Long paymentId) {
        return itemService.cancelPaymentForView(paymentId);
    }



    /*
    История по оплатам
     */
    @PostMapping(value = "/history-payment-for-view")
    public ResponseAnswer<Page<PayForView>> historyPaymentForView(
            @RequestParam(name = "itemId", required = true) long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return itemService.historyPaymentForView(itemId,page, size);
    }
}
