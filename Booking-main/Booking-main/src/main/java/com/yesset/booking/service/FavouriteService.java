package com.yesset.booking.service;


import com.yesset.booking.dto.favourite.FavouriteDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Favourite;

import java.util.List;

public interface FavouriteService {
    ResponseAnswer<Favourite> createFavourite(Long itemId);
    ResponseAnswer<List<FavouriteDto>> getAllFavourites();
    ResponseAnswer<Object> deleteFavourite(Integer id);
}
