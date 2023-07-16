package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.JwtException;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService implements UserDetailsService {

    private final UserRepository repository;

    public ManagerInfoDto getInfoForUsername(String username) {
        return ManagerInfoDto.of(repository.findByUsername(username));
    }

    public ManagerInfoDto getInfo(String username) throws JwtException, CantFindByIdException {
        return ManagerInfoDto.of(repository.findByUsername(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User manager = repository.findByUsername(username);
        if (!manager.getRoles().equals("ROLE_MANAGER")) throw new UsernameNotFoundException("ManagerService - loadUserByUsername() : 잘못된 유저네임");
        return new UserDetailsInterfaceImpl(manager);
    }
}
