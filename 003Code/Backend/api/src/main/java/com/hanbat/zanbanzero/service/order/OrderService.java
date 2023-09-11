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
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.order.OrderRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.DateTools;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private static final int PAGE_SIZE = 10;

    private Long getDefaultMenuId(Long userId) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(userId).orElseThrow(() -> new CantFindByIdException("userId : " + userId));
        Long defaultMenu = policy.getDefaultMenu();
        if (defaultMenu == null) return null;
        else return menuRepository.findById(defaultMenu).orElseThrow(() -> new CantFindByIdException("defaultMenu : " + defaultMenu)).getId();
    }

    @Transactional
    public Order createNewOrder(Long userId, Long menuId, LocalDate date, boolean type) throws CantFindByIdException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CantFindByIdException("menuId : " + menuId));
        return orderRepository.save(Order.createNewOrder(userRepository.getReferenceById(userId), menu.getName(), menu.getCost(), date, type));
    }

    @Transactional
    public OrderDto cancelOrder(String username, int year, int month, int day) throws CantFindByIdException {
        LocalDate date = DateTools.makeLocaldate(year, month, day);
        Long id = userRepository.findByUsername(username).getId();
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) order = orderRepository.save(createNewOrder(id, getDefaultMenuId(id), date, false));
        else order.setRecognizeToCancel();
        return OrderDto.of(order);
    }

    @Transactional
    public OrderDto addOrder(String username, Long menuId, int year, int month, int day) throws CantFindByIdException {
        LocalDate date = DateTools.makeLocaldate(year, month, day);
        Long id = userRepository.findByUsername(username).getId();
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) order = orderRepository.save(createNewOrder(id, menuId, date, true));
        else {
            order.setMenu(menuRepository.findById(menuId).orElseThrow(CantFindByIdException::new));
            order.setRecognizeToUse();
        }
        return OrderDto.of(order);
    }

    public int countPages(String username) {
        Long id = userRepository.findByUsername(username).getId();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        return orderRepository.findByUserIdOrderByIdDesc(id, pageable).getTotalPages();
    }


    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(OrderDto::of)
                .toList();
    }

    @Transactional
    public List<OrderDto> getOrdersPage(String username, int page) {
        Long id = userRepository.findByUsername(username).getId();
        Page<Order> orderPage = orderRepository.findByUserIdOrderByIdDesc(id, PageRequest.of(page, PAGE_SIZE));

        return orderPage.getContent()
                .stream()
                .map(OrderDto::of)
                .toList();
    }

    @Transactional
    public LastOrderDto getLastOrder(String username){
        Long id = userRepository.findByUsername(username).getId();
        Order order = orderRepository.findFirstByUserIdOrderByIdDesc(id);

        if (order == null) return null;
        return LastOrderDto.of(order);
    }

    @Transactional
    public LastOrderDto getOrderById(Long userId, Long id) throws CantFindByIdException, WrongRequestDetails {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        if (order.getUser().getId() != userId) throw new WrongRequestDetails("userId : " + userId + " / orderId : " + id);
        return LastOrderDto.of(order);
    }

    @Transactional
    public void getOrderQr(HttpServletResponse response, Long userId, Long id) throws WriterException, IOException, CantFindByIdException, WrongRequestDetails {
        Long orderUserId = orderRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id)).getUser().getId();
        if (orderUserId != userId) throw new WrongRequestDetails("orderUserId, userId : " + orderUserId + userId);

        String domain = "http://kjj.kjj.r-e.kr:8080";
        String endPoint = "/api/user/order/" + id;
        BufferedImage qrCode = createQRCode(domain + endPoint);

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=qrcode.png");
        ImageIO.write(qrCode, "png", response.getOutputStream());
    }

    private BufferedImage createQRCode(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 100, 100);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public OrderDto getOrder(String username, int year, int month, int day) {
        Long id = userRepository.findByUsername(username).getId();

        Order order = orderRepository.findByUserIdAndOrderDate(id, DateTools.makeDateFormatLocalDate(year, month, day));
        if (order == null) return null;
        else return OrderDto.of(order);
    }
}