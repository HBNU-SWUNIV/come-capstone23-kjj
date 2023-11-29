package com.hanbat.zanbanzero.service.order;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hanbat.zanbanzero.dto.order.LastOrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.entity.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.order.OrderRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImplV1 implements OrderService{

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final DateUtil dateUtil;

    private static final int PAGE_SIZE = 10;

    private Long getDefaultMenuId(Long userId) throws CantFindByIdException {
        UserPolicy policy = userRepository.findById(userId).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 user가 없습니다.
                userId : """, userId)).getUserPolicy();

        if (policy == null) throw new CantFindByIdException("""
                user가 UserPolicy를 가지고 있지 않습니다.
                userId : """, userId);

        Long defaultMenu = policy.getDefaultMenu();
        if (defaultMenu == null) return null;
        else return menuRepository.findById(defaultMenu).orElseGet( () -> {
                    policy.setDefaultMenu(null);
                    return null;
                }
        ).getId();
    }

    @Override
    public Order createNewOrder(Long userId, Long menuId, LocalDate date, boolean type) throws CantFindByIdException, WrongRequestDetails {
        if (menuId == null) throw new WrongRequestDetails("""
                전달된 menuId가 null 입니다.
                menuId : """, menuId);

        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CantFindByIdException("""
                menuId는 전달되었으나, 해당 id를 가진 메뉴가 존재하지 않습니다.
                menuId : """, menuId));

        Order order = Order.createNewOrder(userRepository.getReferenceById(userId), menu.getName(), menu.getCost(), date, type);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long id, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date).orElse(null);

        if (order == null) order = orderRepository.save(createNewOrder(id, getDefaultMenuId(id), date, false));
        else order.setRecognizeToCancel();
        return OrderDto.from(order);
    }

    @Override
    @Transactional
    public OrderDto addOrder(Long id, Long menuId, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date).orElse(null);

        if (order == null) order = orderRepository.save(createNewOrder(id, menuId, date, true));
        else order.setMenuAndRecognizeTrue(menuRepository.getReferenceById(menuId));

        return OrderDto.from(order);
    }

    @Override
    @Transactional(readOnly = true)
    public int countPages(Long id) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        return orderRepository.findByUserIdOrderByIdDesc(id, pageable).getTotalPages();
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersPage(Long id, int page) {
        Page<Order> orderPage = orderRepository.findByUserIdOrderByIdDesc(id, PageRequest.of(page, PAGE_SIZE));

        return orderPage.getContent()
                .stream()
                .map(OrderDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LastOrderDto getLastOrder(Long id){
        Optional<Order> order = orderRepository.findFirstByUserIdOrderByIdDesc(id);

        return order.map(LastOrderDto::from)
                .orElse(null);
    }

    @Override
    public BufferedImage getOrderQr(Long id) throws WriterException {
        String domain = "http://kjj.kjj.r-e.kr:8080";
        String endPoint = "/api/user/order/" + id + "/qr";

        return createQRCode(domain + endPoint);
    }

    @Override
    public BufferedImage createQRCode(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 100, 100);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrderMonth(Long id, int year, int month) {
        List<Order> orders = orderRepository.findByUserIdAndOrderDate_YearAndOrderDate_Month(id, year, month);

        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderDay(Long id, int year, int month, int day) {
        Optional<Order> order = orderRepository.findByUserIdAndOrderDate(id, dateUtil.makeLocalDate(year, month, day));

        return order.map(OrderDto::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public void checkOrder(Long id) throws CantFindByIdException {
        Order order = orderRepository.findByIdWithFetch(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Order 데이터를 찾을 수 없습니다.
                orderId : """, id));
        if (!order.isExpired()) {
            order.setExpiredTrue();

            int point = 50;
            UserMypage myPage = order.getUser().getUserMypage();
            myPage.updatePoint(point);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderInfo(Long id, Long orderId) throws CantFindByIdException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Order 데이터를 찾을 수 없습니다.
                orderId : """, orderId));

        return OrderDto.from(order);
    }

    @Override
    @Transactional
    public OrderDto setPaymentTrue(Long id) throws CantFindByIdException  {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Order 데이터를 찾을 수 없습니다.
                orderId : """, id));
        order.setPaymentTrue();

        return OrderDto.from(order);
    }
}