package com.yesset.booking.endpoint;

import com.yesset.booking.dto.location.MicroDistinctDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.MicroDistinct;
import com.yesset.booking.entity.Review;
import com.yesset.booking.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/open-api/microDistinct")
@RequiredArgsConstructor
public class OpenMicroDistinct {

    private final LocationService locationService;

    /*
    Получить все микрорайоны
     */
    @GetMapping
    public ResponseAnswer<List<MicroDistinctDto>> getMicroDistinctDto(
    ) {
        return locationService.getMicroDistinctDto();
    }
}
