package com.hanbat.zanbanzero.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.order.OrderRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private Order createNewOrder(Long userId, String date, boolean type) {
        UserPolicy userPolicy = userPolicyRepository.findById(userId).orElseThrow(CantFindByIdException::new);
        Menu menu = menuRepository.findById(userPolicy.getDefaultMenu()).orElseThrow(CantFindByIdException::new);
        return new Order(
                null,
                userRepository.getReferenceById(userId),
                userPolicy.getDefaultMenu(),
                menu.getCost(),
                date,
                type
        );
    }

    @Transactional
    public void cancelOrder(Long id, int year, int month, int day) {
        String date = DateTools.makeDateString(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) {
            Order data = createNewOrder(id, date, false);
            orderRepository.save(data);
        }
        else {
            order.setRecognizeToCancel();
        }
    }

    @Transactional
    public void addOrder(Long id, Long menuId, int year, int month, int day) {
        String date = DateTools.makeDateString(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) {
            Order data = createNewOrder(id, date, true);
            orderRepository.save(data);
        }
        else {
            order.setMenu(menuId);
            order.setRecognizeToUse();
        }
    }


    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(order -> {
                    try {
                        return OrderDto.createOrderDto(order);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
