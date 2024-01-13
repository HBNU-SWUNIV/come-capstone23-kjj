package com.hanbat.zanbanzero.repository.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.calculate.CalculatePre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculatePreRepository extends JpaRepository<CalculatePre, Long> {
    Optional<CalculatePre> findTopByOrderByIdDesc();
}
