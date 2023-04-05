package com.hanbat.zanbanzero.repository.planner;

import com.hanbat.zanbanzero.entity.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {

    List<Planner> findAllByDateBetween(Timestamp start, Timestamp end);
}
