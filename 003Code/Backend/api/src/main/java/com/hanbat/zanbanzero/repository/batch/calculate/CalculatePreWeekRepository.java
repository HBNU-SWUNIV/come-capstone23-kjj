package com.hanbat.zanbanzero.repository.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.calculate.CalculatePreWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculatePreWeekRepository extends JpaRepository<CalculatePreWeek, Long> {
    Optional<CalculatePreWeek> findFirstByOrderByIdDesc();
}
