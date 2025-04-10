package org.chiches.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, String name);
    boolean fileExists(String bucketName, String objectKey);
}
