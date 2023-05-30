package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.dto.menu.MenuImageDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MenuImageController {

    private String localPath = "/";

    @Operation(summary="이미지 조회")
    @GetMapping("/api/image")
    public ResponseEntity<FileSystemResource> getImage(@RequestParam("dir") String dir) throws IOException {
        FileSystemResource resource = new FileSystemResource(localPath + dir);
        System.out.println(dir);

        HttpHeaders httpHeaders = new HttpHeaders();
        Path path = Paths.get(localPath + dir);
        httpHeaders.add("Content-Type", Files.probeContentType(path));

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }
}
