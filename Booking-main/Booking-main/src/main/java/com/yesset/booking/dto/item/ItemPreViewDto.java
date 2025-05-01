package com.yesset.booking.dto.item;

import com.yesset.booking.entity.Category;
import com.yesset.booking.entity.File;
import com.yesset.booking.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@ToString
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPreViewDto implements Serializable {

    private Long id;

    private String name;

    private String description;

    private BigDecimal pricePerDay;

    private Boolean available = true;
    private List<File> files;
}
