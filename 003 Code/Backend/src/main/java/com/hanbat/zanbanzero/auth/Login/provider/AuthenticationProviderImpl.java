package com.hanbat.zanbanzero.auth.login.provider;

import com.hanbat.zanbanzero.auth.login.userDetailsService.ManagerDetailsServiceImpl;
import com.hanbat.zanbanzero.auth.login.userDetailsService.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final ManagerDetailsServiceImpl managerDetailsServiceImpl;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        String uri = (String) authentication.getDetails();

        UserDetails principalDetails = null;

        if (uri.endsWith("/user")) {
            principalDetails = userDetailsServiceImpl.loadUserByUsername(username);
        }
        else if (uri.endsWith("/manager")) {
            principalDetails = managerDetailsServiceImpl.loadUserByUsername(username);
        }

        if (password == null || !bCryptPasswordEncoder.matches(password, principalDetails.getPassword())) {
            throw new AuthenticationServiceException("인증 실패");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(principalDetails.getAuthorities().toString()));

        return new UsernamePasswordAuthenticationToken(principalDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
