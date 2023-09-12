package com.hanbat.zanbanzero.repository.order;

import com.hanbat.zanbanzero.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long l);

    Order findByUserIdAndOrderDate(Long id, LocalDate date);

    @Query("SELECT o FROM Order o WHERE o.user.id = :id AND YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    List<Order> findByUserIdAndOrderDate_YearAndOrderDate_Month(Long id, int year, int month);

    Order findFirstByUserIdOrderByIdDesc(Long id);

    Page<Order> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}
