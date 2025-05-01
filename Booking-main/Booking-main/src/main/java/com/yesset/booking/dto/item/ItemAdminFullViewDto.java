package com.yesset.booking.dto.item;
import com.yesset.booking.entity.File;
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
public class ItemAdminFullViewDto implements Serializable {
    private String name;
    private String description;
    private String categoryMap;
    private BigDecimal pricePerDay;
    private String attributes; // JSONB для хранения характеристик
    private Boolean available;
    private List<File> files;
    private Double rate;
    private String verificationDocument;
}

