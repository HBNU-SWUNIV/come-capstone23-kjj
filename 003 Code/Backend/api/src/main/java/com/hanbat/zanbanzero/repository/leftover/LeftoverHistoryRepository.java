package com.hanbat.zanbanzero.repository.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeftoverHistoryRepository extends JpaRepository<LeftoverHistory, Long> {
}
