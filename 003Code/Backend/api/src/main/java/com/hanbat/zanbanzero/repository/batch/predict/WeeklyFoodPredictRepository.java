package com.hanbat.zanbanzero.repository.batch.predict;

import com.hanbat.zanbanzero.entity.batch.predict.WeeklyFoodPredict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyFoodPredictRepository extends JpaRepository<WeeklyFoodPredict, Long> {
    WeeklyFoodPredict findFirstByOrderByIdDesc();
}
