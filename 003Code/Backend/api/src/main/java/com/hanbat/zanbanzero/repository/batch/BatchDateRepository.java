package com.hanbat.zanbanzero.repository.batch;

import com.hanbat.zanbanzero.entity.batch.BatchDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BatchDateRepository extends JpaRepository<BatchDate, Long> {
    Optional<BatchDate> findByDate(LocalDate targetDate);
}
