package com.hanbat.zanbanzero.repository.store;

import com.hanbat.zanbanzero.entity.store.StoreState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreStateRepository extends JpaRepository<StoreState, Long> {
    StoreState findByDate(String todayDate);

    @Query("SELECT s FROM StoreState s ORDER BY s.date DESC LIMIT 7")
    List<StoreState> findTop7ByOrderByCreatedAtDesc();
}
