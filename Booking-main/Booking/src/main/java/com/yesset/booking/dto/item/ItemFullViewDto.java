package com.yesset.booking.dto.item;

import com.yesset.booking.entity.File;
import com.yesset.booking.entity.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemFullViewDto implements Serializable {
    private String name;
    private String description;
    private String categoryMap;
    private String categoryMapKz;
    private String categoryMapRu;
    private BigDecimal pricePerDay;
    private Map<String, String> attributes;
    private Map<String, String> attributesKz;
    private Map<String, String> attributesRu;
    private Boolean available;
    private List<File> files;
    private Double rate;
    private String verificationDocument;
    private User user;
}
