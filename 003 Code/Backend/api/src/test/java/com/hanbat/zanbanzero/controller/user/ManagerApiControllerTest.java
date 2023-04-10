package com.hanbat.zanbanzero.controller.user;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import com.hanbat.zanbanzero.service.user.ManagerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ManagerApiController.class) //클래스 지정하여 스캔
@AutoConfigureMockMvc(addFilters = false) // Security 설정 무시
class ManagerApiControllerTest extends ControllerTestClass {

    @MockBean
    private ManagerService managerService;

    private final ManagerDto dto = new ManagerDto(1L,"test manager", "1234", null);

    @Test
    void getInfo() throws Exception{
        ManagerInfoDto managerInfoDto = new ManagerInfoDto();
        Mockito.when(managerService.getInfo()).thenReturn(managerInfoDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/info")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(managerInfoDto)));

        Mockito.verify(managerService, Mockito.times(1)).getInfo();
    }
}