package com.hanbat.zanbanzero.service.menu;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MenuImageService {

    private String uploadDir = "img/menu";

    private String makeFilePath(String filename) {
        return Paths.get(uploadDir, filename).toString();
    }

    public String uploadImage(MultipartFile file) {
        // cleanPath() 메소드는 악성코드 삽입을 방지하기 위해 파일이름 정리 목적
        String fileName = RandomStringUtils.randomAlphanumeric(10);
        String filePath = makeFilePath(fileName + ".png");
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패");
        }
        return filePath;
    }

    public String updateImage(MultipartFile file, String oldPath) throws IOException {
        Files.copy(file.getInputStream(), Paths.get(oldPath), StandardCopyOption.REPLACE_EXISTING);
        return "파일 업데이트 성공";
    }
}
