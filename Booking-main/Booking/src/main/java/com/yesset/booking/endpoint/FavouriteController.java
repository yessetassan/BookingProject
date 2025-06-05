package com.yesset.booking.endpoint;

import com.yesset.booking.dto.favourite.FavouriteDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.Favourite;
import com.yesset.booking.service.FavouriteService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    /*
    Посмотреть избранное
     */
    @GetMapping
    public ResponseAnswer<List<FavouriteDto>> getByUser() {
        return favouriteService.getAllFavourites();
    }

    /*
    Добавлять в избранное
     */
    @PostMapping
    public ResponseAnswer<Favourite> create(@RequestParam(name = "itemId", required = true) long itemId) {
        return favouriteService.createFavourite(itemId);
    }

    /*
    Удалить в избранное
     */
    @DeleteMapping("/{id}")
    public ResponseAnswer<Object> delete(@PathVariable Integer id) {
        return favouriteService.deleteFavourite(id);
    }
}
