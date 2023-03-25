package com.hanbat.zanbanzero.auth;

import com.hanbat.zanbanzero.auth.login.provider.AuthenticationProviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final AuthenticationProviderImpl authenticationProviderImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationProviderImpl.authenticate(authentication);
    }
}
