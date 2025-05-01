package com.yesset.booking.mapper.item;
import com.yesset.booking.dto.item.ItemPreViewDto;
import com.yesset.booking.entity.File;
import com.yesset.booking.entity.Item;
import com.yesset.booking.repositiry.FileRepository;
import com.yesset.booking.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private static FileService fileService = null;

    public ItemMapper(FileService fileService) {
        this.fileService = fileService;
    }

    public static List<ItemPreViewDto> toDtoList(List<Item> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ItemPreViewDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        List<File> preFile = fileService.getAllFilesByItem(item.getId());
        System.out.println("preFile: " + preFile);
        System.out.println("ItemPreViewDto: " + new ItemPreViewDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPricePerDay(),
                item.getAvailable(),
                preFile));

        return new ItemPreViewDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPricePerDay(),
                item.getAvailable(),
                fileService.getAllFilesByItem(item.getId())
        );
    }

    private static List<File> getFileImages(Long id) {
        try {
            List<File> files = fileService.getAllFilesByItem(id);
            System.out.println(files);
            return fileService.getAllFilesByItem(id);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
