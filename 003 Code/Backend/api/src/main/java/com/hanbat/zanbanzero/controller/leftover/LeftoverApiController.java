package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
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
    public ResponseEntity<String> setLeftover(@RequestBody LeftoverHistoryDto dto) throws CantFindByIdException {
        leftoverService.setLeftover(dto);
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }

    @Operation(summary="count개 만큼의 최근 잔반 감소량 확인", description="")
    @GetMapping("/api/manager/store/leftovers/{count}")
    public ResponseEntity<List<LeftoverHistoryDto>> getAlLeftover(@PathVariable Long count) {
        List<LeftoverHistoryDto> leftoverHistoryDtos = leftoverService.getAllLeftover(count);
        return ResponseEntity.status(HttpStatus.OK).body(leftoverHistoryDtos);
    }
}
