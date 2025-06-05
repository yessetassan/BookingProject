package com.yesset.booking.service;

import com.yesset.booking.entity.File;

import java.util.List;

public interface FileService {
    public List<File> getAllFilesByItem(Long itemId);
}
