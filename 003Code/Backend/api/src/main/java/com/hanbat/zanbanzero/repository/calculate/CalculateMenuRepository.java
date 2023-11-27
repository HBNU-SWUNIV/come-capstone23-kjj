package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculateMenu;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalculateMenuRepository extends JpaRepository<CalculateMenu, Long> {
    @Query("SELECT SUM(c.count) FROM CalculateMenu c")
    Integer getAllUsers();

    @Query("SELECT cm FROM CalculateMenu cm WHERE cm.calculate.id IN (:idList)")
    List<CalculateMenu> getPopularMenus(@Param("idList") List<Long> idList);
}
