package com.yesset.booking.service.impl;

import com.yesset.booking.dto.favourite.FavouriteDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.*;
import com.yesset.booking.mapper.favourite.FavouriteMapper;
import com.yesset.booking.repositiry.FavouriteRepository;
import com.yesset.booking.repositiry.FileRepository;
import com.yesset.booking.repositiry.ItemRepository;
import com.yesset.booking.repositiry.UserHistoryRepository;
import com.yesset.booking.service.FavouriteService;
import com.yesset.booking.util.UserApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yesset.booking.util.ErrorStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteRepository repository;
    private final ItemRepository itemRepository;
    private final FileRepository fileRepository;
    private final FavouriteMapper mapper;
    private final UserHistoryRepository userHistoryRepository;


    @Override
    public ResponseAnswer<Favourite> createFavourite(Long itemId) {
        ResponseAnswer<Favourite> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new RuntimeException("User not logged in");
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            Favourite favourite = repository.findByUserIdAndItemId(
                    user.getId(),
                    itemId
            ).stream().findFirst().orElse(null);
            if (favourite == null) favourite = favourite(user, itemId);
            repository.save(favourite);
            UserHistory userHistory = UserHistory.builder()
                    .user(user)
                    .item(item)
                    .actionType("favorite")
                    .actionTime(LocalDateTime.now())
                    .build();
            userHistoryRepository.save(userHistory);
            response.setBody(favourite);
        }catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(FAVOURITE_CREATE_ERROR.toString());
        }
        return response;
    }

    private Favourite favourite(User user, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setItem(item);
        return favourite;
    }

    @Override
    public ResponseAnswer<List<FavouriteDto>> getAllFavourites() {
        ResponseAnswer<List<FavouriteDto>> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) throw new RuntimeException("User not logged in");
            List<Favourite> favourites = repository.findByUserId(user.getId());
            response.setBody(toFavouriteDto(favourites));
        }catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    private List<FavouriteDto> toFavouriteDto(List<Favourite> favourites) {
        List<FavouriteDto> favouriteDtos = new ArrayList<>();
        try {
            for (Favourite favourite : favourites) {
                List<File> files = fileRepository.findAllByItem(favourite.getItem());
                favouriteDtos.add(mapper.toDto(favourite, files));
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return favouriteDtos;
    }

    @Override
    public ResponseAnswer<Object> deleteFavourite(Integer itemId) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            User user = UserApp.getCurrentUser();
            if (user == null) {
                throw new RuntimeException("User not logged in");
            }
            Favourite favourite = repository.findByUserIdAndItemId(
                    user.getId(),
                    itemId
            ).stream().findFirst().orElseThrow(() -> new RuntimeException("Item not found"));
            repository.delete(favourite);
        }catch (Exception e) {
            response.setSuccess(false);
            response.setStatus(FAVOURITE_DELETE_ERROR.toString());
            log.error("Ошибка: {}",e.getMessage());
        }
        return response;
    }
}
