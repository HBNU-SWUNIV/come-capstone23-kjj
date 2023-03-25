package com.hanbat.zanbanzero.auth.login.userDetailsService;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.auth.login.userDetails.ManagerDetailsInterfaceImpl;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerDetailsServiceImpl implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public ManagerDetailsInterfaceImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username);
        return new ManagerDetailsInterfaceImpl(manager);
    }
}
