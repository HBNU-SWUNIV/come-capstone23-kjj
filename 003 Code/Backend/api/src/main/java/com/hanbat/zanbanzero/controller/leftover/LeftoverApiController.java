package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.leftover.LeftoverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LeftoverApiController {

    private final LeftoverService leftoverService;

    @Operation(summary="잔반 감소량 설정", description="")
    @PostMapping("/api/manager/leftover/set")
    public ResponseEntity<String> setLeftover(@RequestBody LeftoverDto dto) throws WrongRequestDetails, WrongParameter {
        if (dto.getDate() != null) {
            throw new WrongRequestDetails("날짜값은 생략되어야 합니다.");
        }

        if (dto.getLeftover() == null) {
            throw new WrongRequestDetails("데이터가 부족합니다.");
        }

        leftoverService.setLeftover(dto);
        return ResponseEntity.status(HttpStatus.OK).body("저장되었습니다.");
    }

    @Operation(summary="전체 잔반 감소량 데이터 페이지 수 조회(페이지 생성용)", description="페이지 사이즈보다 작을 경우 null")
    @GetMapping("/api/manager/leftover/count")
    public ResponseEntity<Integer> getAllLeftoverPage() {
        int result = leftoverService.getAllLeftoverPage();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="특정 페이지 잔반 발생량 데이터 조회", description="페이지 사이즈 = 5")
    @GetMapping("/api/manager/leftover/get/{page}")
    public ResponseEntity<List<LeftoverDto>> getAllLeftover(@PathVariable int page) {
        List<LeftoverDto> result = leftoverService.getLeftoverPage(page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="특정 페이지 예측량 & 발생량 데이터 조회", description="페이지 사이즈 = 5")
    @GetMapping("/api/manager/leftover/get/group/{page}")
    public ResponseEntity<List<LeftoverAndPreDto>> getAllLeftoverAndPre(@PathVariable int page) {
        List<LeftoverAndPreDto> result = leftoverService.getAllLeftoverAndPre(page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="지난주 잔반량 데이터 조회", description="월~금 5개 데이터")
    @GetMapping("/api/manager/leftover/get/lastweek/{type}")
    public ResponseEntity<List<LeftoverDto>> getLastWeeksLeftovers(@PathVariable int type) throws WrongParameter {
        List<LeftoverDto> result = leftoverService.getLastWeeksLeftovers(type);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
