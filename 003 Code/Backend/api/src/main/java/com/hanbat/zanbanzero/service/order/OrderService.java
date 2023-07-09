package com.hanbat.zanbanzero.service.order;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hanbat.zanbanzero.dto.order.LastOrderDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
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

    private int pageSize = 10;

    private Menu getDefaultMenu(Long userId) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(userId).orElseThrow(CantFindByIdException::new);
        return menuRepository.findById(policy.getDefaultMenu()).orElseThrow(CantFindByIdException::new);
    }

    @Transactional
    public Order createNewOrder(Long userId, Long menuId, String date, boolean type) throws CantFindByIdException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(CantFindByIdException::new);
        return orderRepository.save(Order.createNewOrder(userRepository.getReferenceById(userId), menu.getName(), menu.getCost(), date, type));
    }

    @Transactional
    public void cancelOrder(Long id, int year, int month, int day) throws CantFindByIdException {
        String date = DateTools.makeResponseDateFormatString(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) orderRepository.save(createNewOrder(id, getDefaultMenu(id).getId(), date, false));
        else order.setRecognizeToCancel();
    }

    @Transactional
    public void addOrder(Long id, Long menuId, int year, int month, int day) throws CantFindByIdException {
        String date = DateTools.makeResponseDateFormatString(year, month, day);
        Order order = orderRepository.findByUserIdAndOrderDate(id, date);

        if (order == null) orderRepository.save(createNewOrder(id, menuId, date, true));
        else {
            order.setMenu(menuRepository.findById(menuId).orElseThrow(CantFindByIdException::new));
            order.setRecognizeToUse();
        }
    }

    public int countPages(Long id) {
        Pageable pageable = PageRequest.of(0, pageSize);

        return orderRepository.findByUserIdOrderByIdDesc(id, pageable).getTotalPages();
    }


    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(order -> OrderDto.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderDto> getOrdersPage(Long id, int page) {
        Page<Order> orderPage = orderRepository.findByUserIdOrderByIdDesc(id, PageRequest.of(page, pageSize));

        return orderPage.getContent()
                .stream()
                .map(order -> OrderDto.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public LastOrderDto getLastOrder(Long id){
        Order order = orderRepository.findFirstByUserIdOrderByIdDesc(id);

        if (order == null) return null;
        return LastOrderDto.of(order);
    }

    public LastOrderDto getOrderById(Long id) throws CantFindByIdException {
        Order order = orderRepository.findById(id).orElseThrow(CantFindByIdException::new);
        return LastOrderDto.of(order);
    }

    public void getOrderQr(HttpServletResponse response, Long id) throws Exception {
        String domain = "http://kjj.kjj.r-e.kr:8080";
        String endPoint = "/api/user/order/" + id;
        BufferedImage qrCode = createQRCode(domain + endPoint);

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=qrcode.png");
        ImageIO.write(qrCode, "png", response.getOutputStream());
    }

    private BufferedImage createQRCode(String data) throws Exception{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 100, 100);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
