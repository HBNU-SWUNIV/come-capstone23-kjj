package com.hanbat.zanbanzero.repository.calculate;

import com.hanbat.zanbanzero.entity.calculate.Calculate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalculateRepository extends JpaRepository<Calculate, Long> {
    Calculate findByDate(String todayDate);

    @Query("SELECT c FROM Calculate c ORDER BY c.id DESC LIMIT 5")
    List<Calculate> findTop5ByIdOrderByIdDesc();

    Page<Calculate> findAllByOrderByIdDesc(Pageable pageable);
}
