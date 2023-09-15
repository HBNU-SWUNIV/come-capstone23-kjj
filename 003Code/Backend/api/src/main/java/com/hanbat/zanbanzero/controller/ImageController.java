package com.hanbat.zanbanzero.controller;

import com.google.zxing.WriterException;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    /**
     * 이미지 조회
     *
     * @param dir - DB에 저장된 경로
     * @return 이미지 파일
     */
    @Operation(summary="이미지 조회")
    @GetMapping
    public ResponseEntity<FileSystemResource> getImage(@RequestParam("dir") String dir) throws IOException {
        String localPath = "/";
        FileSystemResource resource = new FileSystemResource(localPath + dir);

        HttpHeaders httpHeaders = new HttpHeaders();
        Path path = Paths.get(localPath + dir);
        httpHeaders.add("Content-Type", Files.probeContentType(path));

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    /**
     * QR코드 이미지 조회
     *
     * @param id - Order ID
     * @throws WriterException - QR 생성 라이브러리 사용 중 발생
     * @throws IOException - 응답에 QR코드 작성 중 발생
     */
    @Operation(summary="QR코드 이미지 조회")
    @GetMapping("/order/{id}")
    public void getOrderQr(HttpServletResponse response, @PathVariable Long id) throws WriterException, IOException {
        orderService.getOrderQr(response, id);
    }
}
