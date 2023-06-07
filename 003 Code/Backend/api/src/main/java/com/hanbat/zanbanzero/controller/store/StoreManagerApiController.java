package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreOffDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/")
public class StoreManagerApiController {

    private final StoreService storeService;

    @Operation(summary="식당 정보 세팅 확인", description="관리자 로그인시 세팅 여부 확인 / 없으면 null")
    @GetMapping("setting")
    public ResponseEntity<StoreDto> isSetting() {
        StoreDto result = storeService.isSetting();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 정보 세팅", description="세팅정보 없을 시 세팅")
    @PostMapping("setting")
    public ResponseEntity<String> setSetting(@RequestBody StoreDto dto) throws SameNameException {
        storeService.setSetting(dto);
        return ResponseEntity.status(HttpStatus.OK).body("설정되었습니다.");
    }

    @Operation(summary="금일 이용자 수 조회", description="10:30분마다 정산하여 갱신됨")
    @GetMapping("state/today")
    public ResponseEntity<Integer> getToday() {
        Integer result = storeService.getToday();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="지난주 이용자 수 조회", description="월~금 5개 데이터")
    @GetMapping("state/last-week/user")
    public ResponseEntity<List<StoreWeekendDto>> getLastWeeksUser() throws WrongParameter {
        List<StoreWeekendDto> result = storeService.getLastWeeksUser();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="총 누적 이용자 수 조회")
    @GetMapping("state/all")
    public ResponseEntity<Integer> getAllUsers() {
        int result = storeService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "최근 5영업일 메뉴별 판매량 조회", description = "메뉴 3종류만")
    @GetMapping("state/menu")
    public ResponseEntity<List<CalculateMenuForGraphDto>> getPopularMenus() {
        List<CalculateMenuForGraphDto> result = storeService.getPopularMenus();
        return ResponseEntity.ok(result);
    }

    @Operation(summary="단체명 수정")
    @PatchMapping("store/title")
    public ResponseEntity<StoreDto> updateStoreTitle(@RequestBody StoreDto dto) throws CantFindByIdException{
        StoreDto result = storeService.updateStoreTitle(dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="식당 소개 수정")
    @PatchMapping("store/info")
    public ResponseEntity<StoreDto> updateStoreInfo(@RequestBody StoreDto dto) throws CantFindByIdException, WrongRequestDetails {
        StoreDto result = storeService.updateStoreInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="휴무일 설정", description="n월 n일의 휴무일 설정")
    @PostMapping("store/off/{year}/{month}/{day}")
    public ResponseEntity<String> setOff(@RequestBody StoreOffDto off, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        storeService.setOff(off.isOff(), year, month, day);
        System.out.println(off);
        return ResponseEntity.ok().body("저장되었습니다.");
    }

    @Operation(summary="월간 휴무일 조회", description="n월 한달의 휴무일 조회")
    @GetMapping("store/off/{year}/{month}")
    public ResponseEntity<List<StoreStateDto>> getClosedDays(@PathVariable int year, @PathVariable int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("잘못된 입력입니다.");
        List<StoreStateDto> result = storeService.getClosedDays(year, month);
        return ResponseEntity.ok().body(result);
    }
}