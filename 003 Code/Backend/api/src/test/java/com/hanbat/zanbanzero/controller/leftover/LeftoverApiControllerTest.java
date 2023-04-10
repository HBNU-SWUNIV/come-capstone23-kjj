package com.hanbat.zanbanzero.controller.leftover;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverShowDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.leftover.LeftoverService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(LeftoverApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class LeftoverApiControllerTest extends ControllerTestClass {

    @MockBean
    LeftoverService leftoverService;

    private final Long testId = 1L;

    @Test
    void getLeftover() throws Exception{
        // 1. 정상 요청
        {        
            // Given
            LeftoverShowDto expected = new LeftoverShowDto();
            Mockito.when(leftoverService.getLeftover()).thenReturn(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/store/leftover")).andReturn();

            // Then
            assertEquals(objectMapper.writeValueAsString(expected), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(leftoverService, Mockito.times(1)).getLeftover();
        }
    }

    @Test
    void setLeftover() throws Exception{
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "수정되었습니다.";
            LeftoverShowDto expected = new LeftoverShowDto();
            Mockito.doNothing().when(leftoverService).setLeftover(expected);
    
            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/store/set/leftover")
                    .content(objectMapper.writeValueAsString(expected))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
    
            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());
    
            Mockito.verify(leftoverService, Mockito.times(1)).setLeftover(expected);
        }

        // 2. null 데이터 요청
        {
            // Given
            String expectedMsg = "데이터가 부족합니다.";
            LeftoverShowDto expected = new LeftoverShowDto();
            Mockito.doThrow(new WrongRequestDetails(expectedMsg)).when(leftoverService).setLeftover(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/store/set/leftover")
                    .content(objectMapper.writeValueAsString(expected))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(400, result.getResponse().getStatus());

            Mockito.verify(leftoverService, Mockito.times(2)).setLeftover(expected);
        }
    }

    @Test
    void getAlLeftover() throws Exception{
        // 1. 정상 요청
        {
            // Given
            Long testCount = 1L;
            List<LeftoverHistoryDto> expected = new ArrayList<>();
            Mockito.when(leftoverService.getAllLeftover(testCount)).thenReturn(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/store/leftovers/1")).andReturn();

            // Then
            assertEquals(objectMapper.writeValueAsString(expected), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(leftoverService, Mockito.times(1)).getAllLeftover(testCount);
        }
    }
}