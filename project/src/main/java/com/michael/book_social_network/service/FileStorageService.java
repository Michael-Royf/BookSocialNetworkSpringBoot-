package com.michael.book_social_network.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file,  Long id);
}
