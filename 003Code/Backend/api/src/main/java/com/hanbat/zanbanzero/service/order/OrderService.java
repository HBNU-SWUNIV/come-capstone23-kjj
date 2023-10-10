package com.hanbat.zanbanzero.service.order;

import com.google.zxing.WriterException;
import com.hanbat.zanbanzero.dto.order.LastOrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order createNewOrder(Long userId, Long menuId, LocalDate date, boolean type) throws CantFindByIdException, WrongRequestDetails;

    OrderDto cancelOrder(Long id, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails;

    OrderDto addOrder(String username, Long menuId, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails;

    int countPages(Long id);

    List<OrderDto> getOrders(Long id);

    List<OrderDto> getOrdersPage(String username, int page);

    LastOrderDto getLastOrder(String username);

    BufferedImage getOrderQr(Long id) throws WriterException;

    BufferedImage createQRCode(String data) throws WriterException;

    List<OrderDto> getOrderMonth(Long id, int year, int month);

    OrderDto getOrderDay(String username, int year, int month, int day);

    void checkOrder(Long id) throws CantFindByIdException;

    OrderDto getOrderInfo(Long orderId) throws CantFindByIdException;
}
