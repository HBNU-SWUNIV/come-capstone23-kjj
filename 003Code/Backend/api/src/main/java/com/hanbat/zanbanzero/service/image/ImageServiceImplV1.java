package com.hanbat.zanbanzero.service.image;

import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImplV1 implements ImageService{
    @Override
    public String makeFilePath(String filename, String uploadDir) {
        return Paths.get(uploadDir, filename).toString();
    }

    @Override
    public String uploadImage(MultipartFile file, String uploadDir) throws UploadFileException {
        String fileName = RandomStringUtils.randomAlphanumeric(10);
        String filePath = makeFilePath(fileName + ".png", uploadDir);
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UploadFileException(String.format("fileName : %s / filePath : %s", fileName, filePath), e);
        }
        return filePath;
    }

    @Override
    public void updateImage(MultipartFile file, String oldPath) throws UploadFileException {
        try {
            Files.copy(file.getInputStream(), Paths.get(oldPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UploadFileException(String.format("fileOldPath : %s", oldPath), e);
        }
    }
}