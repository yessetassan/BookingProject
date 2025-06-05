package com.yesset.booking.dto.filter;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class FilterDto  implements Serializable {
    private String name;
    private String nameKz;
    private String nameRu;
    private String type;
    private Map<String, Map<String, String>> options;
    private String optionsNumber;
}
