package com.hanbat.zanbanzero.repository.order;

import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.entity.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Order> {

    //이전 쿼리 @Query("select o from OrderDetails o join fetch o.orders where o.id = :id")
    @Query(value = "select * " +
            "from (select * from order_details where order_details.orders_id = :id) o " +
            "join orders on o.orders_id = orders.id", nativeQuery = true)
    Optional<OrderDetails> getOrderDetails(@Param("id") Long id);
}
