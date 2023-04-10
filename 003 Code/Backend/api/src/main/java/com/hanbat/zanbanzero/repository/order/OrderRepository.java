package com.hanbat.zanbanzero.repository.order;

import com.hanbat.zanbanzero.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long l);
}
