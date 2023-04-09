package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.login.userDetails.ManagerDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.manager.ManagerPasswordDto;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ManagerRepository managerRepository;
    private final Long finalId = 1L;

    public ManagerInfoDto getInfo() throws JwtException {
        Manager manager = managerRepository.findById(finalId).orElseThrow(CantFindByIdException::new);

        return ManagerInfoDto.createManagerInfoDto(manager);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username);
        return new ManagerDetailsInterfaceImpl(manager);
    }

    @Transactional
    public void setManagerNickname(String username) {
        Manager manager = managerRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        manager.setUsername(username);
    }

    @Transactional
    public void setManagerPassword(ManagerPasswordDto dto) {
        Manager manager = managerRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        boolean result = bCryptPasswordEncoder.matches(dto.getOldPass(), manager.getPassword());
        if (!result) throw new WrongParameter("잘못된 비밀번호입니다.");

        manager.setPassword(bCryptPasswordEncoder.encode(dto.getNewPass()));
    }
}
