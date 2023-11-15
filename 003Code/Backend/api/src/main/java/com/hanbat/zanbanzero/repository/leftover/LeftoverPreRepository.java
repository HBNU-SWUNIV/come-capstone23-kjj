package com.hanbat.zanbanzero.repository.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LeftoverPreRepository extends JpaRepository<LeftoverPre, Long> {
    LeftoverPre findByDate(LocalDate localDate);
}
