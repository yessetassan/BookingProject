package com.yesset.booking.mapper.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesset.booking.dto.filter.FilterDto;
import com.yesset.booking.entity.Filter;
import com.yesset.booking.entity.Translate;
import com.yesset.booking.repositiry.TranslateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilterMapper {
    private final TranslateRepository translateRepository;

    public FilterDto toDto(Filter filter, List<Translate> translates) {
        if (filter == null) {
            return null;
        }
        return  FilterDto
                .builder()
                .type(filter.getType())
                .name(filter.getName())
                .nameKz(filter.getNameKz())
                .nameRu(filter.getNameRu())
                .options(optionsForFilter(filter.getOptions(),translates))
                .optionsNumber(filter.getOptions())
                .build();
    }

    private Map<String, Map<String, String>> optionsForFilter(String options, List<Translate> translates) {
        List<String> list = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(options);
            JsonNode valuesNode = rootNode.get("values");
            if (valuesNode.isArray()) {
                for (JsonNode node : valuesNode) {
                    list.add(node.asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Map<String, String>> result = new HashMap<>();
        for (String option : list) {
            Map<String, String> map = new HashMap<>();
            try {
                map.put("en", option);
                map.put("kz", translateWord(option, "kz",translates));
                map.put("ru", translateWord(option, "ru",translates));
            }catch (Exception e){
                e.printStackTrace();
            }
            result.put(option, map);
        }
        return result;
    }

    private String translateWord(String option, String lang, List<Translate> translates) {
        try {
            Translate translate = translates.stream()
                    .filter(current -> current.getName().equals(option))
                    .findAny()
                    .orElse(null);
            if (translate == null) return option;
            if (lang.equals("kz")){
                return translate.getNameKz();
            }else if (lang.equals("ru")){
                return translate.getNameRu();
            }
            return option;
        }catch (Exception e){
            log.info("Ошибка: {}", e.getMessage());
            return option;
        }
    }

    public List<FilterDto> toDtoList(List<Filter> filters, List<Translate> translates) {
        return filters.stream()
                .map(filter -> toDto(filter, translates)) // ✅ передаем оба аргумента
                .collect(Collectors.toList());
    }


}
