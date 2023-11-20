package com.hanbat.zanbanzero.service.user.manager;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImplV1 implements ManagerService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ManagerInfoDto getInfoForUsername(String username) {
        return ManagerInfoDto.from(userRepository.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerInfoDto getInfo(String username) {
        return ManagerInfoDto.from(userRepository.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User manager = userRepository.findByUsername(username);
        if (manager == null) throw new UsernameNotFoundException("""
                username을 가진 Manager를 찾을 수 없습니다.
                username : """ + username);
        if (!manager.getRoles().equals("ROLE_MANAGER")) throw new UsernameNotFoundException("""
                권한이 없습니다.
                username : """ + username);
        return new UserDetailsInterfaceImpl(manager);
    }
}
