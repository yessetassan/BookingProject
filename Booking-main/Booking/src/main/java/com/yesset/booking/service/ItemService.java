package com.yesset.booking.service;

import com.yesset.booking.dto.item.*;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.PayForView;
import com.yesset.booking.entity.Rental;
import org.springframework.data.domain.Page;

public interface ItemService {
    ResponseAnswer<Page<ItemPreViewDto>> getFilteredItems(ItemRequestDto request);
    ResponseAnswer<ItemFullViewDto> getFullData(Long itemId);

    ResponseAnswer<Object> createItemByUser(ItemAddDto request);

    ResponseAnswer<Object> delete(Long id);

    ResponseAnswer<Page<ItemPreViewDto>> getCheckInItems(ItemRequestDto request);

    ResponseAnswer<Page<ItemPreViewDto>> getRejectedItems(ItemRequestDto request);

    ResponseAnswer<Page<ItemPreViewDto>> getApprovedItems(ItemRequestDto request);

    ResponseAnswer<Object> reject(Long id);

    ResponseAnswer<Object> approve(Long id);

    ResponseAnswer<ItemAdminFullViewDto> getFullDataByAdmin(Long itemId);

    ResponseAnswer<Object> payForView(ItemViewPayDto request);

    ResponseAnswer<Object> cancelPaymentForView(Long paymentId);

    ResponseAnswer<Page<PayForView>> historyPaymentForView(long itemId, int page, int size);

    ResponseAnswer<BookFullDto> getFullDataToBook(Long itemId);

    ResponseAnswer<Object> bookNow(Long itemId, BookRequestDto requestDto);

    ResponseAnswer<Object> activeBook(Long id);

    ResponseAnswer<Page<Rental>> historyForBookingOwner(Long itemId, int page, int size);

    ResponseAnswer<Page<Rental>> historyForBookingUser(Long itemId, int page, int size);

    ResponseAnswer<Object> cancelBook(Long id);

    ResponseAnswer<Page<ItemPreViewDto>> getAllConflicted(ItemRequestDto request);

    void checkAvailability();
}
