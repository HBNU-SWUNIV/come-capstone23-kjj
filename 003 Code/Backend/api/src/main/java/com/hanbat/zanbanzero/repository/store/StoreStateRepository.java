package com.hanbat.zanbanzero.repository.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StoreStateRepository extends JpaRepository<StoreState, Long> {
    StoreState findByDate(LocalDate date);

    List<StoreState> findAllByDateBetween(String start, String end);
}
