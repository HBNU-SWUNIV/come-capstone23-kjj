package com.hanbat.zanbanzero.repository.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.BatchDate;
import com.hanbat.zanbanzero.entity.batch.calculate.Calculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CalculateRepository extends JpaRepository<Calculate, Long> {
    Optional<Calculate> findByBatchDate(BatchDate batchDate);

    @Query("SELECT c.id FROM Calculate c ORDER BY c.id DESC LIMIT 5")
    List<Long> findTop5IdOrderByIdDesc();

    @Query("SELECT c.today FROM Calculate c ORDER BY c.id DESC LIMIT 2")
    List<Integer> findLastTwoToday();

    @Query("SELECT c.sales FROM Calculate c ORDER BY c.id DESC LIMIT 2")
    List<Integer> findLastTwoSales();

    Optional<Calculate> findTopByOrderByIdDesc();
}