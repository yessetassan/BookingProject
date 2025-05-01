package com.yesset.booking.service.impl;

import com.yesset.booking.dto.location.MicroDistinctDto;
import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.entity.MicroDistinct;
import com.yesset.booking.mapper.location.MicroDistinctMapper;
import com.yesset.booking.repositiry.MicroDistinctRepository;
import com.yesset.booking.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.yesset.booking.util.ErrorStatus.PULL_DATA_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final MicroDistinctRepository microDistinctRepository;
    @Override
    public ResponseAnswer<List<MicroDistinctDto>> getMicroDistinctDto() {
        ResponseAnswer<List<MicroDistinctDto>> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);
        try {
            List<MicroDistinctDto> all = getAllMicroDistinct();
            response.setBody(all);
        }catch (Exception e) {
            response.setSuccess(Boolean.FALSE);
            response.setStatus(PULL_DATA_ERROR.toString());
        }
        return response;
    }

    private List<MicroDistinctDto> getAllMicroDistinct() {
        List<MicroDistinct> allMicroDistincts = microDistinctRepository.findAll();
        return allMicroDistincts.stream()
                .filter(current -> current.getParentId() == null)
                .map(parent -> {
                    List<MicroDistinct> children = allMicroDistincts.stream()
                            .filter(child -> parent.getId().equals(child.getParentId()))
                            .collect(Collectors.toList());
                    return MicroDistinctMapper.toDto(parent, children);
                })
                .collect(Collectors.toList());
    }
}
