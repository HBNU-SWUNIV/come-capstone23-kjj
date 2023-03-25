package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.service.user.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerApiController {
    private final ManagerService managerService;

    @GetMapping("/api/manager/info")
    public ResponseEntity<ManagerInfoDto> getInfo(@RequestBody ManagerDto dto) throws JwtException {
        ManagerInfoDto managerDto = managerService.getInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(managerDto);
    }

}
