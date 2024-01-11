package com.hanbat.zanbanzero.repository.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.calculate.CalculatePreWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatePreWeekRepository extends JpaRepository<CalculatePreWeek, Long> {
    CalculatePreWeek findFirstByOrderByIdDesc();
}
