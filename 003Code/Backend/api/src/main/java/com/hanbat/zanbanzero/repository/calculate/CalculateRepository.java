package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.Calculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalculateRepository extends JpaRepository<Calculate, Long> {
    Optional<Calculate> findByDate(LocalDate todayDate);

    @Query("SELECT c.id FROM Calculate c ORDER BY c.id DESC LIMIT 5")
    List<Long> findTop5IdOrderByIdDesc();

    @Query("SELECT c.today FROM Calculate c ORDER BY c.id DESC LIMIT 2")
    List<Integer> findLastTwoToday();

    @Query("SELECT c.sales FROM Calculate c ORDER BY c.id DESC LIMIT 2")
    List<Integer> findLastTwoSales();

    Optional<Calculate> findTopByOrderByIdDesc();
}