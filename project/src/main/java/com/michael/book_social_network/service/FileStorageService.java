package com.michael.book_social_network.service;

import com.michael.book_social_network.entity.Book;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file,  Long id);
}
