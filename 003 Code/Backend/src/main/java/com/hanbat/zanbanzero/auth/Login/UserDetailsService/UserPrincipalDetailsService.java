package com.hanbat.zanbanzero.auth.login.userdetailsservice;

import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.auth.login.userdetails.UserPrincipalDetails;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserPrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserPrincipalDetails(user);
    }
}
