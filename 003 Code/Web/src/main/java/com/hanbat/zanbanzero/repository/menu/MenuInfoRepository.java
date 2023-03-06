package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuInfoRepository extends JpaRepository<MenuInfo, Long> {

    @Query("select m from MenuInfo m join fetch m.menu where m.id = :id")
    Optional<MenuInfo> findByIdAndFetch(@Param("id") Long id);
}
