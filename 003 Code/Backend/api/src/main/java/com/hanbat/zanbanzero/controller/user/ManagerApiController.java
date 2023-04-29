package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.service.user.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerApiController {
    private final ManagerService managerService;

    @Operation(summary="매니저 대표정보 조회", description="username만 입력받아 정보조회")
    @GetMapping("/api/manager/info")
    public ResponseEntity<ManagerInfoDto> getInfo() throws JwtException, CantFindByIdException {
        ManagerInfoDto managerDto = managerService.getInfo();
        return ResponseEntity.status(HttpStatus.OK).body(managerDto);
    }

    @Operation(summary="매니저 닉네임 변경", description="새 username 입력")
    @PatchMapping("/api/manager/set/username")
    public ResponseEntity<String> setManagerNickname(@RequestBody ManagerInfoDto dto) throws CantFindByIdException {
        managerService.setManagerNickname(dto.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("변경되었습니다.");
    }
}
