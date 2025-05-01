package com.yesset.booking.service;

import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.user.UserCardRequestDto;
import com.yesset.booking.entity.UserCard;

import java.util.List;

public interface UserCardService {
    ResponseAnswer<Object> save(UserCardRequestDto card);
    ResponseAnswer<List<UserCard>> getUserCards();
    ResponseAnswer<Object> delete(Long id);
}
