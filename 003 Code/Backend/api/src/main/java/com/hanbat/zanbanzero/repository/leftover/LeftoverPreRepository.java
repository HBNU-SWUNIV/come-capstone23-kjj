package com.hanbat.zanbanzero.repository.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeftoverPreRepository extends JpaRepository<LeftoverPre, Long> {
    Page<LeftoverPre> findAllByOrderByCalculateIdDesc(Pageable pageable);
}
