package com.hanbat.zanbanzero.service.image;

import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String makeFilePath(String filename, String uploadDir);
    String uploadImage(MultipartFile file, String uploadDir) throws UploadFileException;
    void updateImage(MultipartFile file, String oldPath) throws UploadFileException;
}
