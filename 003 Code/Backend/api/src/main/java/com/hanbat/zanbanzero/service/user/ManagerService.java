package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.user.UserRepository;
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
    private final UserRepository repository;
    private final String role = "ROLE_MANAGER";

    public ManagerInfoDto getInfoForUsername(String username) {
        return ManagerInfoDto.createManagerInfoDto(repository.findByUsername(username));
    }

    public ManagerInfoDto getInfo() throws JwtException, CantFindByIdException {
        User manager = repository.findByRoles(role).orElseThrow(CantFindByIdException::new);

        return ManagerInfoDto.createManagerInfoDto(manager);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User manager = repository.findByUsername(username);
        return new UserDetailsInterfaceImpl(manager);
    }

    @Transactional
    public void setManagerNickname(String username) throws CantFindByIdException {
        User manager = repository.findByRoles(role).orElseThrow(CantFindByIdException::new);
        manager.setUsername(username);
    }
}
