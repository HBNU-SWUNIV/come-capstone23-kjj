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

    OrderDto addOrder(Long id, Long menuId, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails;

    int countPages(Long id);

    List<OrderDto> getOrders(Long id);

    List<OrderDto> getOrdersPage(Long id, int page);

    LastOrderDto getLastOrder(Long id);

    BufferedImage getOrderQr(Long id) throws WriterException;

    BufferedImage createQRCode(String data) throws WriterException;

    List<OrderDto> getOrderMonth(Long id, int year, int month);

    OrderDto getOrderDay(Long id, int year, int month, int day);

    void checkOrder(Long id) throws CantFindByIdException;

    OrderDto getOrderInfo(Long orderId) throws CantFindByIdException;
}
