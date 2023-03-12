package com.hanbat.zanbanzero.auth.login.UserDetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsInterface extends UserDetails {
    String getMemberRoles();

    Long getMemberId();

}
