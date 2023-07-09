package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.CalculatePre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatePreRepository extends JpaRepository<CalculatePre, Long> {
    CalculatePre findTopByOrderByIdDesc();
}
