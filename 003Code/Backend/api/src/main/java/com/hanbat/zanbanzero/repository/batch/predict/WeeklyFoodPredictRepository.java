package com.hanbat.zanbanzero.repository.batch.predict;

import com.hanbat.zanbanzero.entity.batch.predict.WeeklyFoodPredict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeeklyFoodPredictRepository extends JpaRepository<WeeklyFoodPredict, Long> {
    Optional<WeeklyFoodPredict> findFirstByOrderByIdDesc();
}
