package com.yesset.booking.endpoint;

import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.service.FilterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/open-api/filters")
public class FilterController {

    private final FilterService filterService;

    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }


    @GetMapping("/category/{categoryId}")
    public ResponseAnswer<List<FilterDto>> getFiltersByCategory(@PathVariable(name = "categoryId", required = false) Long categoryId) {
        return filterService.getFiltersByCategoryId(categoryId);
    }
}
