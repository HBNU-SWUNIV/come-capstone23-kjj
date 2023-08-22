package com.hanbat.zanbanzero.repository.planner;

import com.hanbat.zanbanzero.entity.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {

    @Query("select p from Planner p where p.date = :date")
    Planner findOnePlanner(@Param("date")LocalDate date);

    List<Planner> findAllByDateBetween(LocalDate start, LocalDate end);
}
