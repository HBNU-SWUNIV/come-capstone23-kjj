package com.hanbat.zanbanzero.repository.store;

import com.hanbat.zanbanzero.entity.store.Calculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreStateRepository extends JpaRepository<Calculate, Long> {
    Calculate findByDate(String todayDate);

    @Query("SELECT s FROM Calculate s ORDER BY s.date DESC LIMIT 7")
    List<Calculate> findTop7ByOrderByCreatedAtDesc();
}
