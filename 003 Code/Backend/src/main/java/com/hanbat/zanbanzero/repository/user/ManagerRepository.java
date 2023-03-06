package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Manager findByUsername(@Param("username") String username);

}
