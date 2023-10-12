package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuWithInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Boolean existsByName(String name);

    @Query("select new com.hanbat.zanbanzero.entity.menu.MenuWithInfo(m.id, m.name, m.cost, m.image, m.sold, m.usePlanner, m.menuFood.id, i.info, i.details) from Menu m join m.menuInfo i")
    List<MenuWithInfo> findAllWithMenuInfo();

    @Query("select new com.hanbat.zanbanzero.entity.menu.MenuWithInfo(m.id, m.name, m.cost, m.image, m.sold, m.usePlanner, m.menuFood.id, i.info, i.details) from Menu m join m.menuInfo i where m.id = :id")
    MenuWithInfo findOneWithMenuInfoById(@Param("id") Long id);

    Menu findByUsePlanner(boolean b);

    Boolean existsByUsePlannerTrue();
}
