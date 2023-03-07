package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private  final JwtUtil jwtUtil;

    private final ManagerRepository managerRepository;

    public ManagerInfoDto getInfo(ManagerDto dto, String token) throws JwtException {
        if (!jwtUtil.checkJwt(dto.getUsername(), token)) {
            throw new JwtException("토큰과 유저명이 다릅니다.");
        }

        Manager manager = managerRepository.findByUsername(dto.getUsername());

        return ManagerInfoDto.createManagerInfoDto(manager);
    }

}
