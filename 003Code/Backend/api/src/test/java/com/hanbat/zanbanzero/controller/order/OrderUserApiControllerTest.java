package com.hanbat.zanbanzero.controller.order;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderUserApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class OrderUserApiControllerTest extends ControllerTestClass {

    @MockBean
    OrderService orderService;
    private final Long testId = 1L;

    @Test
    void addOrder() throws Exception {
//        // 1. 정상 요청
//        {
//            //Given
//            Mockito.doNothing().when(orderService).addOrder(dto, testId);
//
//            // When
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/1/order/add")
//                            .content(objectMapper.writeValueAsString(dto))
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andReturn();
//
//            // Then
//            String expected = "등록되었습니다.";
//
//            assertEquals(expected, result.getResponse().getContentAsString());
//            assertEquals(200, result.getResponse().getStatus());
//
//            Mockito.verify(orderService, Mockito.times(1)).addOrder(dto, testId);
//        }
//
//        // 2. menu가 null인 데이터 요청
//        {
//            //Given
//            final OrderDetailsDto nullDto = new OrderDetailsDto(null, null, 2000, null, 0);
//            Mockito.doThrow(new WrongRequestDetails("데이터가 부족합니다.")).when(orderService).addOrder(nullDto, testId);
//
//            // When
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/1/order/add")
//                            .content(objectMapper.writeValueAsString(nullDto))
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andReturn();
//
//            // Then
//            assertEquals(400, result.getResponse().getStatus());
//
//            Mockito.verify(orderService, Mockito.times(1)).addOrder(nullDto, testId);
//        }
    }

    @Test
    void getOrders() throws Exception{
        // 1. 정상 요청
        {
            //Given
            List<OrderDto> expected = new ArrayList<>();
            Mockito.when(orderService.getOrders(testId)).thenReturn(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1/order/show")).andReturn();

            // Then
            assertEquals(objectMapper.writeValueAsString(expected), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(orderService, Mockito.times(1)).getOrders(testId);
        }
    }
}