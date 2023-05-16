package com.hanbat.zanbanzero.repository.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreStateRepository extends JpaRepository<StoreState, Long> {
    StoreState findByDate(String dateString);

    List<StoreState> findAllByDateBetween(String start, String end);
}
