package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculatePreWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatePreWeekRepository extends JpaRepository<CalculatePreWeek, Long> {
    CalculatePreWeek findFirstByOrderByIdDesc();
}
