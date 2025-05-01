package com.yesset.booking.service.impl;

import com.yesset.booking.entity.File;
import com.yesset.booking.entity.Item;
import com.yesset.booking.repositiry.FileRepository;
import com.yesset.booking.repositiry.ItemRepository;
import com.yesset.booking.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final ItemRepository itemRepository;

    public FileServiceImpl(FileRepository fileRepository, ItemRepository itemRepository) {
        this.fileRepository = fileRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<File> getAllFilesByItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) throw new RuntimeException("Item not found");
        return fileRepository.findAllByItem(item);
    }
}
