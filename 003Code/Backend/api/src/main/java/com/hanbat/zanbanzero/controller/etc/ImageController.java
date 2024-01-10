package com.hanbat.zanbanzero.controller.etc;

import com.google.zxing.WriterException;
import com.hanbat.zanbanzero.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name = "이미지 컨트롤러", description = "이미지를 조회하는 API")
@Controller
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final OrderService orderService;

    /**
     * 이미지 조회
     *
     * @param dir - DB에 저장된 경로
     * @return 이미지 파일
     */
    @Operation(summary="이미지 조회", description = "dir 파라미터를 전달받아 해당 경로의 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이미지 조회 실패")
    })
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
     * @param orderId - Order ID
     * @throws WriterException - QR 생성 라이브러리 사용 중 발생
     * @throws IOException - 응답에 QR코드 작성 중 발생
     */
    @Operation(summary="QR코드 이미지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "QR 생성 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러 발생")
    })
    @GetMapping("/order/{orderId}")
    public void getOrderQr(HttpServletResponse response, @PathVariable Long orderId) throws WriterException, IOException {
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=qrcode.png");
        ImageIO.write(orderService.getOrderQr(orderId), "png", response.getOutputStream());
    }
}
