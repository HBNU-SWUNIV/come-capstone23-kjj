package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.leftover.LeftoverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/manager/")
public class LeftoverApiController {

    private final LeftoverService leftoverService;

    /**
     * 잔반 감소량 설정
     *
     * @param dto - String date(yyyy-MM-dd), Double leftover
     * @return LeftoverDto
     * @throws WrongRequestDetails - leftover가 null이면 발생
     */
    @Operation(summary="잔반 감소량 설정")
    @PostMapping("leftover")
    public ResponseEntity<LeftoverDto> setLeftover(@RequestBody LeftoverDto dto) throws WrongRequestDetails {
        if (dto.getLeftover() == null) throw new WrongRequestDetails("Leftover : null");

        return ResponseEntity.ok(leftoverService.setLeftover(dto));
    }

    /**
     * 페이징을 위한 전체 잔반 감소량 데이터 페이지 수 조회
     * 페이지 크기 : 5
     *
     * @return int
     */
    @Operation(summary="전체 잔반 감소량 데이터 페이지 수 조회(페이지 생성용)", description = "페이지 사이즈보다 작을 경우 null")
    @GetMapping("leftover/count")
    public ResponseEntity<Integer> getAllLeftoverPage() {
        return ResponseEntity.ok(leftoverService.getAllLeftoverPage());
    }

    /**
     * 특정 페이지의 잔반발생량 조회
     *
     * @param page - 몇 페이지인지 (0~)
     * @return List<LeftoverDto>
     */
    @Operation(summary="특정 페이지 잔반 발생량 데이터 조회", description="페이지 사이즈 = 5")
    @GetMapping("leftover/{page}")
    public ResponseEntity<List<LeftoverDto>> getAllLeftover(@PathVariable int page) {
        return ResponseEntity.ok(leftoverService.getLeftoverPage(page));
    }

    /**
     * 특정 페이지 잔반량, 예측량 조회
     * 페이지 크기 = 5
     *
     * @param page - 몇 페이지인지 (0~)
     * @return List<LeftoverAndPreDto>
     * @throws CantFindByIdException - 예측 데이터가 존재하지 않을 경우 발생
     */
    @Operation(summary="특정 페이지 예측량 & 발생량 데이터 조회", description="페이지 사이즈 = 5")
    @GetMapping("leftover/group/{page}")
    public ResponseEntity<List<LeftoverAndPreDto>> getAllLeftoverAndPre(@PathVariable int page) throws CantFindByIdException {
        return ResponseEntity.ok(leftoverService.getAllLeftoverAndPre(page));
    }

    /**
     * 이전 주 월 ~ 금 잔반 발생량 조회
     * 데이터가 없을 경우 잔반량은 0.0
     *
     * @param type - 원하는 주의 -1 값 (ex. 1주 전 데이터일 경우 type = 0)
     * @return List<LeftoverDto>
     * @throws WrongParameter - type 음수일 경우 발생
     */
    @Operation(summary="지난주 잔반량 데이터 조회", description="월~금 5개 데이터")
    @GetMapping("leftover/last-week/{type}")
    public ResponseEntity<List<LeftoverDto>> getLastWeeksLeftovers(@PathVariable int type) throws WrongParameter {
        if (type < 0) throw new WrongParameter("type(0 ~) : " + type);
        return ResponseEntity.ok(leftoverService.getLastWeeksLeftovers(type));
    }
}
