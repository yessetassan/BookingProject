package com.yesset.booking.service.impl;

import com.yesset.booking.dto.reason.CreateReasonRequest;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Item;
import com.yesset.booking.entity.Reason;
import com.yesset.booking.entity.User;
import com.yesset.booking.repositiry.ItemRepository;
import com.yesset.booking.repositiry.ReasonRepository;
import com.yesset.booking.service.ReasonService;
import com.yesset.booking.util.UserApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.yesset.booking.util.ErrorStatus.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository reasonRepository;
    private final ItemRepository itemRepository;

    @Override
    public ResponseAnswer<Reason> createReason(CreateReasonRequest request) {
        ResponseAnswer<Reason> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");
            Reason reason = new Reason();
            Item item = itemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException(SOURCE_NOT_FOUND_ERROR.name()));
            reason.setItem(item);
            reason.setFileUrl(request.getFileUrl());
            reason.setFileType(request.getFileType());
            reason.setStatus(request.getStatus());
            reason.setReasonText(request.getReasonText());
            reason.setIsActive(Boolean.TRUE);
            reason.setCreatedAt(LocalDateTime.now());
            reason.setUpdatedAt(LocalDateTime.now());
            reason.setUser(user);
            Reason saved = reasonRepository.save(reason);
            response.setBody(saved);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(REASON_CREATION_ERROR.toString());
            response.setReason(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<Reason>> getAllReasons(String status, Long itemId, int page, int size) {
        ResponseAnswer<Page<Reason>> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Reason> reasons;

            if (status != null && !status.isEmpty()) {
                reasons = reasonRepository.findAllByStatusAndIsActiveAndItemId(status,true, itemId, pageable);
            } else if (itemId != null) {
                reasons = reasonRepository.findAllByItemIdAndIsActive(itemId,true, pageable);
            } else {
                response.setSuccess(false);
                response.setStatus(PULL_DATA_ERROR.toString());
                return response;
            }

            response.setBody(reasons);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(PULL_DATA_ERROR.toString());
            response.setReason(e.getMessage());
        }
        return response;
    }


    @Override
    public ResponseAnswer<Object> updateReason(Long id, CreateReasonRequest updatedReason) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Reason existing = reasonRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(SOURCE_NOT_FOUND_ERROR.name()));
            if (existing == null) {
                response.setSuccess(false);
                response.setStatus(SOURCE_NOT_FOUND_ERROR.name());
                return response;
            }

            existing.setFileUrl(updatedReason.getFileUrl());
            existing.setFileType(updatedReason.getFileType());
            existing.setStatus(updatedReason.getStatus());
            existing.setReasonText(updatedReason.getReasonText());
            existing.setIsActive(updatedReason.getIsActive());
            existing.setUpdatedAt(LocalDateTime.now());

            reasonRepository.save(existing);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(REASON_UPDATE_ERROR.toString());
            response.setReason(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Object> deleteReason(Long id) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);

        try {
            Reason reason = reasonRepository.findById(id).orElse(null);
            if (reason == null) {
                response.setSuccess(false);
                response.setStatus(SOURCE_NOT_FOUND_ERROR.name());
                return response;
            }
            reasonRepository.deleteById(id);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(REASON_DELETION_ERROR.toString());
            response.setReason(e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseAnswer<Page<Reason>> mineReasons(String status, int page, int size) {
        ResponseAnswer<Page<Reason>> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Reason> reasons;
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");

            if (status != null && !status.isEmpty()) {
                reasons = reasonRepository.findAllByStatusAndUserId(status, user.getId(), pageable);
            } else {
                reasons = reasonRepository.findAllByUserId(user.getId(), pageable);
            }

            response.setBody(reasons);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(PULL_DATA_ERROR.toString());
            response.setReason(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseAnswer<Page<Reason>> ownerReasons(String status, int page, int size) {
        ResponseAnswer<Page<Reason>> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Reason> reasons;
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception("User not found");

            if (status != null && !status.isEmpty()) {
                reasons = reasonRepository.findByStatusAndItemOwner(status, user.getId(), pageable);
            } else {
                reasons = reasonRepository.findPagedReasonsByItemOwner(user.getId(), pageable);
            }

            response.setBody(reasons);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(PULL_DATA_ERROR.toString());
            response.setReason(e.getMessage());

        }
        return response;
    }
}
