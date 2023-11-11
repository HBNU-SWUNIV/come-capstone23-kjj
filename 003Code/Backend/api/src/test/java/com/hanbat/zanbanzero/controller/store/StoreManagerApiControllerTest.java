package com.hanbat.zanbanzero.controller.store;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.controller.store.manager.StoreManagerApiController;
import com.hanbat.zanbanzero.service.store.StoreService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(StoreManagerApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class StoreManagerApiControllerTest extends ControllerTestClass {

    @MockBean
    private StoreService storeService;
}