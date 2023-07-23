package com.hanbat.zanbanzero.auth.login.authenticationManager;

import com.hanbat.zanbanzero.auth.login.provider.LoginAuthenticationProviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAuthenticationManagerImpl implements AuthenticationManager {

    private final LoginAuthenticationProviderImpl loginAuthenticationProviderImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return loginAuthenticationProviderImpl.authenticate(authentication);
    }
}
