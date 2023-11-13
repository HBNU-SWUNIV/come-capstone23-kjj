package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Boolean existsByName(String name);

    @Query("select m from Menu m join fetch m.menuInfo join fetch m.menuFood")
    List<Menu> findAllWithMenuInfo();

    Menu findByUsePlanner(boolean b);

    Boolean existsByUsePlannerTrue();
}
