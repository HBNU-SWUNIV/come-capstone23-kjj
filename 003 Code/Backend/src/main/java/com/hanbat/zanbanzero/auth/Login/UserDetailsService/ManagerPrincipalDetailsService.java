package com.hanbat.zanbanzero.auth.login.userdetailsservice;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.auth.login.userdetails.ManagerPrincipalDetails;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerPrincipalDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public ManagerPrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByUsername(username);
        return new ManagerPrincipalDetails(manager);
    }
}
