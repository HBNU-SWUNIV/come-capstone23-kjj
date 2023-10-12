package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagerServiceImplV1 implements UserDetailsService, ManagerService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    @Override
    public ManagerInfoDto getInfoForUsername(String username) {
        return ManagerInfoDto.of(repository.findByUsername(username));
    }

    @Override
    public ManagerInfoDto getInfo(String username) {
        return ManagerInfoDto.of(repository.findByUsername(username));
    }

    @Override
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User manager = repository.findByUsername(username);
        if (manager == null) throw new UsernameNotFoundException("username : " + username );
        if (!manager.getRoles().equals("ROLE_MANAGER")) throw new UsernameNotFoundException("ManagerService - loadUserByUsername() / username : " + username);
        return new UserDetailsInterfaceImpl(manager);
    }

    public Map<String, String> testToken() {
        String managerName = "manager";
        return Map.of("accessToken", jwtUtil.createToken(loadUserByUsername(managerName)));
    }
}
