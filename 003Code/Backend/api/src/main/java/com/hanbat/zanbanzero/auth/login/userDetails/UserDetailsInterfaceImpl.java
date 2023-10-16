package com.hanbat.zanbanzero.auth.login.userDetails;

import com.hanbat.zanbanzero.entity.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class UserDetailsInterfaceImpl implements UserDetailsInterface {

    private User user;

    public UserDetailsInterfaceImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(user::getRoles);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getMemberRoles() {
        return user.getRoles();
    }

    @Override
    public Long getMemberId() {
        return user.getId();
    }

}
