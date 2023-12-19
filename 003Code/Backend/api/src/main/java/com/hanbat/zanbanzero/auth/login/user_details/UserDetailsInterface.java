package com.hanbat.zanbanzero.auth.login.user_details;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsInterface extends UserDetails {
    String getMemberRoles();

    Long getMemberId();

}
