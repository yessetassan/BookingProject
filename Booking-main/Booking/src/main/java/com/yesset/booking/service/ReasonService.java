package com.yesset.booking.service;


import com.yesset.booking.dto.reason.CreateReasonRequest;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Reason;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReasonService {
    ResponseAnswer<Reason> createReason(CreateReasonRequest reason);
    ResponseAnswer<Page<Reason>> getAllReasons(String status,Long itemId, int page, int size);
    ResponseAnswer<Object> updateReason(Long id, CreateReasonRequest updatedReason);
    ResponseAnswer<Object> deleteReason(Long id);
    ResponseAnswer<Page<Reason>> mineReasons(String status, int page, int size);
    ResponseAnswer<Page<Reason>> ownerReasons(String status, int page, int size);
}
