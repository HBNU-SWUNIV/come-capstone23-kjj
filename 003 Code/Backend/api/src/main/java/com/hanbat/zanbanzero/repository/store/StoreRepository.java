package com.hanbat.zanbanzero.repository.store;


import com.hanbat.zanbanzero.entity.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store s join fetch s.manager where s.id = :id")
    Store findByIdWithManager(@Param("id") Long id);
}
