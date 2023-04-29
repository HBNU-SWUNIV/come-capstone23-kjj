package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculateMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalculateMenuRepository extends JpaRepository<CalculateMenu, Long> {
    @Query("SELECT SUM(c.count) FROM CalculateMenu c")
    Integer getAllUsers();

    @Query("SELECT SUM(cm.count) FROM CalculateMenu cm where cm.calculate.id = :calculateId")
    Integer sumCountByCalculateId(@Param("calculateId") Long calculateId);
}
