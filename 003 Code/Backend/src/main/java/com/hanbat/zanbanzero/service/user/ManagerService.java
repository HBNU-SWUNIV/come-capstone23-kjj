package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.login.userDetails.ManagerDetailsInterfaceImpl;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    public ManagerInfoDto getInfo(ManagerDto dto) throws JwtException {
        Manager manager = managerRepository.findByUsername(dto.getUsername());

        return ManagerInfoDto.createManagerInfoDto(manager);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username);
        return new ManagerDetailsInterfaceImpl(manager);
    }
}
