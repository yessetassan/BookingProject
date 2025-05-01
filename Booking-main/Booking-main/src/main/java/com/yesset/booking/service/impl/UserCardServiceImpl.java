package com.yesset.booking.service.impl;

import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.user.UserCardRequestDto;
import com.yesset.booking.entity.User;
import com.yesset.booking.entity.UserCard;
import com.yesset.booking.repositiry.UserCardRepository;
import com.yesset.booking.service.UserCardService;
import com.yesset.booking.util.UserApp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.yesset.booking.util.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class UserCardServiceImpl implements UserCardService {

    private final UserCardRepository userCardRepository;

    @Override
    public ResponseAnswer<Object> save(UserCardRequestDto card) {
        ResponseAnswer<Object> responseAnswer = new ResponseAnswer<>();
        responseAnswer.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception();
            UserCard userCard = createUserCard(card,user);
            userCardRepository.save(userCard);
        }catch (Exception e){
            responseAnswer.setSuccess(Boolean.FALSE);
            responseAnswer.setStatus(CARD_CREATION_ERROR.toString());
        }
        return responseAnswer;
    }

    private UserCard createUserCard(UserCardRequestDto card, User user) {
        return UserCard.builder()
                .user(user)
                .cardHolderName(card.getCardHolderName())
                .cardNumber(card.getCardNumber())
                .expiryDate(card.getExpiryDate())
                .cvv(card.getCvv())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public ResponseAnswer<List<UserCard>> getUserCards() {
        ResponseAnswer<List<UserCard>> responseAnswer = new ResponseAnswer<>();
        responseAnswer.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new Exception();
            responseAnswer.setBody(userCardRepository.findByUserId(user.getId()));
        }catch (Exception e){
            responseAnswer.setSuccess(Boolean.FALSE);
            responseAnswer.setStatus(PULL_DATA_ERROR.toString());

        }
        return responseAnswer;
    }

    @Override
    public ResponseAnswer<Object> delete(Long id) {
        ResponseAnswer<Object> responseAnswer = new ResponseAnswer<>();
        responseAnswer.setSuccess(Boolean.TRUE);
        try {
            userCardRepository.deleteById(id);
        }catch (Exception e){
            responseAnswer.setSuccess(Boolean.FALSE);
            responseAnswer.setStatus(CARD_DELETION_ERROR.toString());
        }
        return responseAnswer;
    }
}
