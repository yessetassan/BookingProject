package com.yesset.booking.dto.item;


import com.yesset.booking.dto.filter.AttributeFilterDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestDto implements Serializable {
    private int page;
    private int size;
    private Long ownerId;
    private String name;
    private Long categoryId;
    private List<AttributeFilterDto> filterAttributes;


    @Override
    public String toString() {
        return new StringJoiner(", ", ItemRequestDto.class.getSimpleName() + "[", "]")
                .add("page=" + page)
                .add("size=" + size)
                .add("name='" + name + "'")
                .add("categoryId=" + categoryId)
                .add("filterAttributes=" + filterAttributes)
                .toString();
    }
}
