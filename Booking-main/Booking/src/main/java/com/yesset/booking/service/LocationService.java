package com.yesset.booking.service;

import com.yesset.booking.dto.location.MicroDistinctDto;
import com.yesset.booking.dto.response.ResponseAnswer;

import java.util.List;

public interface LocationService {
    ResponseAnswer<List<MicroDistinctDto>> getMicroDistinctDto();
}
