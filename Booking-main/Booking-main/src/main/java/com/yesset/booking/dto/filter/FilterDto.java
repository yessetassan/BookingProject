package com.yesset.booking.dto.filter;

import java.io.Serializable;

public class FilterDto  implements Serializable {
    public FilterDto() {
    }

    public FilterDto(String name, String type, String options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }

    private String name;
    private String type;
    private String options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
