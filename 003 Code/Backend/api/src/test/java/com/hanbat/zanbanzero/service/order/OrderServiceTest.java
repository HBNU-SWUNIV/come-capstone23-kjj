package com.hanbat.zanbanzero.service.order;

import com.hanbat.zanbanzero.repository.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
//        orderMenuRepository.save(orderDetails);
//        orderRepository.save(order);
    }

    @Test
    void getOrders() {
        
    }
}