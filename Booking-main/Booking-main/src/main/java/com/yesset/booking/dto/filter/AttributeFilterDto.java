package com.yesset.booking.dto.filter;
import com.google.gson.Gson;
import java.util.Map;

public class AttributeFilterDto {
    
    private String name;
    private String type; // "multi-select", "range"
    private String options; // JSON string containing values

    // Parse options to a Map for easy handling
    public Map<String, Object> getParsedOptions() {
        return new Gson().fromJson(options, Map.class);
    }

    public AttributeFilterDto() {
    }

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

    public AttributeFilterDto(String name, String type, String options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }
}


