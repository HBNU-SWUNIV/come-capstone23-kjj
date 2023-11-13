package com.hanbat.zanbanzero.service.user.manager;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImplV1 implements ManagerService {

    private final UserRepository repository;

    @Override
    public ManagerInfoDto getInfoForUsername(String username) {
        return ManagerInfoDto.from(repository.findByUsername(username));
    }

    @Override
    public ManagerInfoDto getInfo(String username) {
        return ManagerInfoDto.from(repository.findByUsername(username));
    }

    @Override
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User manager = repository.findByUsername(username);
        if (manager == null) throw new UsernameNotFoundException("username : " + username );
        if (!manager.getRoles().equals("ROLE_MANAGER")) throw new UsernameNotFoundException("ManagerService - loadUserByUsername() / username : " + username);
        return new UserDetailsInterfaceImpl(manager);
    }
}
