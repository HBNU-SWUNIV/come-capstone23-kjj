package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
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
    @PostMapping("/api/manager/store/set/leftover")
    public ResponseEntity<String> setLeftover(@RequestBody LeftoverHistoryDto dto) throws WrongRequestDetails {
        leftoverService.setLeftover(dto);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @Operation(summary="전체 잔반 감소량 데이터 페이지 수 조회(페이지 생성용)", description="페이지 사이즈보다 작을 경우 null")
    @GetMapping("/api/manager/store/leftovers/count")
    public ResponseEntity<Integer> getAlLeftoverPage() {
        int result = leftoverService.getAllLeftoverPage();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="특정 페이지 데이터 조회", description="페이지 사이즈 = 5")
    @GetMapping("/api/manager/store/leftovers/{page}")
    public ResponseEntity<List<LeftoverHistoryDto>> getAlLeftover(@PathVariable int page) {
        List<LeftoverHistoryDto> result = leftoverService.getLeftoverPage(page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
