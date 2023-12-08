package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Boolean existsByName(String name);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.menuInfo LEFT JOIN FETCH m.menuFood")
    List<Menu> findAllWithMenuInfoAndMenuFood();

    Optional<Menu> findByUsePlanner(boolean b);

    Boolean existsByUsePlannerTrue();

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.menuInfo WHERE m.id = :id")
    Optional<Menu> findByIdWithMenuInfo(Long id);
}
