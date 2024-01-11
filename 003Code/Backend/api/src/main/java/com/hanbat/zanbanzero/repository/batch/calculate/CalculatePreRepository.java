package com.hanbat.zanbanzero.repository.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.calculate.CalculatePre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatePreRepository extends JpaRepository<CalculatePre, Long> {
    CalculatePre findTopByOrderByIdDesc();
}
