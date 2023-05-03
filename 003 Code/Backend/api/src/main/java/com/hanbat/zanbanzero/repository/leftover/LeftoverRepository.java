package com.hanbat.zanbanzero.repository.leftover;

import com.hanbat.zanbanzero.entity.leftover.Leftover;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeftoverRepository extends JpaRepository<Leftover, Long> {

    Page<Leftover> findAllByOrderByLeftoverPreIdDesc(Pageable pageable);

    Leftover findByLeftoverPreId(Long id);
}
