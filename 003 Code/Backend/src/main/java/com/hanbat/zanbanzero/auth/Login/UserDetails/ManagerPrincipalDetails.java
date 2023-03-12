package com.hanbat.zanbanzero.auth.login.UserDetails;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class ManagerPrincipalDetails implements UserDetailsInterface {

    private Manager manager;

    public ManagerPrincipalDetails(Manager manager) {
        this.manager = manager;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> manager.getRoles());
        return authorities;
    }

    @Override
    public String getPassword() {
        return manager.getPassword();
    }

    @Override
    public String getUsername() {
        return manager.getUsername();
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
        return manager.getRoles();
    }

    @Override
    public Long getMemberId() {
        return manager.getId();
    }

}
