package com.hanbat.zanbanzero.repository.leftover;

import com.hanbat.zanbanzero.entity.leftover.LeftoverHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeftoverHistoryRepository extends JpaRepository<LeftoverHistory, Long> {

    @Query(value =
            "select * " +
            "from leftover_history " +
            "order by id DESC " +
            "limit :count"
    , nativeQuery = true)
    List<LeftoverHistory> getAllLeftoverCount(@Param("count") Long count);
}
