package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.MenuFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuFoodRepository extends JpaRepository<MenuFood, Long> {
    boolean existsByName(String name);
}
