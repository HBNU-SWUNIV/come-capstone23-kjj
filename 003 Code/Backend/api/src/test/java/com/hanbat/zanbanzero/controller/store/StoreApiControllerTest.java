package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(StoreApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class StoreApiControllerTest extends ControllerTestClass {

    @MockBean
    private StoreService storeService;

    private final StoreDto dto = new StoreDto("test store", "store info");


    @Test
    void setCongestion() throws Exception{
    }

    @Test
    void getCongestion() throws Exception{
    }
}