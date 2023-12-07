package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDtos;
import com.hanbat.zanbanzero.service.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(MenuUserApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenUserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MenuService menuService;

    @Test
    @DisplayName("[MENU_USER] 유저 전용 전체 메뉴 조회 API")
    void getMenus() throws Exception{
        // 1. 정상 요청
        {
            // Given
            List<MenuUserInfoDto> menuUserInfoDtoList = new ArrayList<>();
            MenuUserInfoDtos expected = new MenuUserInfoDtos(menuUserInfoDtoList);
            Mockito.when(menuService.getMenus()).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/menu"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            Mockito.verify(menuService, Mockito.times(1)).getMenus();
        }
    }
}