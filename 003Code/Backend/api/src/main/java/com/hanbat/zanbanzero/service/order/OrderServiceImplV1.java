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
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImplV1 implements OrderService{

    private final UserRepository userRepository;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final UserMyPageRepository userMyPageRepository;
    private final DateUtil dateUtil;

    private static final int PAGE_SIZE = 10;

    private Long getDefaultMenuId(Long userId) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(userId).orElseThrow(() -> new CantFindByIdException(userId));
        Long defaultMenu = policy.getDefaultMenu();
        if (defaultMenu == null) return null;
        else return menuRepository.findById(defaultMenu).orElseThrow(() -> new CantFindByIdException("defaultMenu", defaultMenu)).getId();
    }

    @Override
    @Transactional
    public Order createNewOrder(Long userId, Long menuId, LocalDate date, boolean type) throws CantFindByIdException, WrongRequestDetails {
        if (menuId == null) throw new WrongRequestDetails("menuId", menuId);
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CantFindByIdException("menuId", menuId));
        return orderRepository.save(Order.createNewOrder(userRepository.getReferenceById(userId), menu.getName(), menu.getCost(), date, type));
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long id, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) order = orderRepository.save(createNewOrder(id, getDefaultMenuId(id), date, false));
        else order.setRecognizeToCancel();
        return OrderDto.of(order);
    }

    @Override
    @Transactional
    public OrderDto addOrder(Long id, Long menuId, int year, int month, int day) throws CantFindByIdException, WrongRequestDetails {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) order = orderRepository.save(createNewOrder(id, menuId, date, true));
        else {
            order.setMenu(menuRepository.findById(menuId).orElseThrow(() -> new CantFindByIdException("menuId", menuId)));
            order.setRecognizeToUse();
        }
        return OrderDto.of(order);
    }

    @Override
    public int countPages(Long id) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        return orderRepository.findByUserIdOrderByIdDesc(id, pageable).getTotalPages();
    }


    @Override
    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(OrderDto::of)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderDto> getOrdersPage(Long id, int page) {
        Page<Order> orderPage = orderRepository.findByUserIdOrderByIdDesc(id, PageRequest.of(page, PAGE_SIZE));

        return orderPage.getContent()
                .stream()
                .map(OrderDto::of)
                .toList();
    }

    @Override
    @Transactional
    public LastOrderDto getLastOrder(Long id){
        Order order = orderRepository.findFirstByUserIdOrderByIdDesc(id);

        if (order == null) return null;
        return LastOrderDto.of(order);
    }

    @Override
    @Transactional
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
    public List<OrderDto> getOrderMonth(Long id, int year, int month) {

        List<Order> order = orderRepository.findByUserIdAndOrderDate_YearAndOrderDate_Month(id, year, month);
        return order.stream()
                .map(OrderDto::of)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto getOrderDay(Long id, int year, int month, int day) {
        Order order = orderRepository.findByUserIdAndOrderDate(id, dateUtil.makeLocalDate(year, month, day));
        if (order == null) return null;
        else return OrderDto.of(order);
    }

    @Override
    @Transactional
    public void checkOrder(Long id) throws CantFindByIdException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CantFindByIdException("orderId", id));
        if (!order.isExpired()) {
            order.setExpiredTrue();

            Long userId = order.getUser().getId();
            int point = 50;
            UserMypage myPage = userMyPageRepository.findById(userId).orElseThrow(() -> new CantFindByIdException("userMypageId", userId));
            myPage.updatePoint(point);
        }
    }

    @Override
    @Transactional
    public OrderDto getOrderInfo(Long id, Long orderId) throws CantFindByIdException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CantFindByIdException("orderId", orderId));
        return OrderDto.of(order);
    }

    @Override
    @Transactional
    public OrderDto setPaymentTrue(Long id) throws CantFindByIdException  {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CantFindByIdException("orderId", id));
        order.setPaymentTrue();
        return OrderDto.of(order);
    }
}