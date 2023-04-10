package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(StoreApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class StoreApiControllerTest extends ControllerTestClass {

    @MockBean
    private StoreService storeService;

    private final StoreDto dto = new StoreDto("test store", "store info");
    private final StoreStateDto stateDto = new StoreStateDto(null, null, 30);


    @Test
    void setCongestion() throws Exception{
        // 1. 정상 요청
        {
            // Given
            Mockito.doNothing().when(storeService).setCongestion(stateDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/store/set/congestion")
                            .content(objectMapper.writeValueAsString(stateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            String expected = "수정되었습니다.";

            assertEquals(expected, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(storeService, Mockito.times(1)).setCongestion(stateDto);
        }

        // 2. null 요청
        {
            // Given
            String error = "데이터가 부족합니다.";
            final StoreStateDto nullDto = new StoreStateDto(null, null, 124);
            Mockito.doThrow(new WrongRequestDetails(error)).when(storeService).setCongestion(nullDto);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/store/set/congestion")
                            .content(objectMapper.writeValueAsString(nullDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(error, result.getResolvedException().getMessage());
            assertEquals(400, result.getResponse().getStatus());

            Mockito.verify(storeService, Mockito.times(1)).setCongestion(nullDto);
        }
    }

    @Test
    void getCongestion() throws Exception{
        // 1. 정상 요청
        {
            // Given
            Mockito.when(storeService.getCongestion()).thenReturn(stateDto.getCongestion());

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/store/congestion")).andReturn();

            // Then
            assertEquals(stateDto.getCongestion().toString(), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(storeService, Mockito.times(1)).getCongestion();
        }
    }
}