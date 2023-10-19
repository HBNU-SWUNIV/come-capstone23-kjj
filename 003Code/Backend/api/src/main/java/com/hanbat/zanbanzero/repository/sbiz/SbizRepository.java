package com.hanbat.zanbanzero.repository.sbiz;

import com.hanbat.zanbanzero.entity.sbiz.WeeklyFoodPredict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SbizRepository extends JpaRepository<WeeklyFoodPredict, Long> {
    WeeklyFoodPredict findFirstByOrderByIdDesc();
}
