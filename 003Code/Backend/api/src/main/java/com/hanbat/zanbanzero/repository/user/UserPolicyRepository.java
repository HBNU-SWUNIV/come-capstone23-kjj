package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPolicyRepository extends JpaRepository<UserPolicy, Long> {
    List<UserPolicy> findAllByDefaultMenu(Long menuId);
}
