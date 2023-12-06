package com.hanbat.zanbanzero.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDtos;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(MenuUserApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenUserApiControllerTest {

    private final String name = "test menuUpdateDto name";
    private final int cost = 3000;
    private final String info = "test menuUpdateDto info";
    private final String details = "test menuUpdateDto details";
    private final boolean usePlanner = false;
    private final String image = "image";
    private final boolean sold = true;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    MenuService menuService;

    private final Long testId = 1L;
    private final String uploadDir = "img/menu";

    @Test
    @DisplayName("유저 전용 전체 메뉴 조회 API")
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

    @Test
    @DisplayName("메뉴 추가 API")
    void addMenu() throws Exception {
        // 1. 정상 요청
        {
            // Given
            MenuUpdateDto expected = new MenuUpdateDto(
                    name,
                    cost,
                    info,
                    details,
                    usePlanner
            );
            MenuDto dto = new MenuDto(
                    null,
                    name,
                    cost,
                    image,
                    sold
            );
            Mockito.when(menuService.addMenu(expected, "path")).thenReturn(dto);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/menu")
                    .content(objectMapper.writeValueAsString(expected))
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals(dto.toString(), result.getResponse().getContentAsString());
                    });

            Mockito.verify(menuService, Mockito.times(1)).addMenu(expected, "path");
        }

        // 2. 중복 상품명 등록
        {
            // Given
            MenuUpdateDto expected = new MenuUpdateDto(
                    name,
                    cost,
                    info,
                    details,
                    usePlanner
            );
            Mockito.doThrow(new SameNameException("""
                이미 해당 이름을 가진 메뉴가 존재합니다.
                dto : """)).when(menuService).addMenu(expected, "path");

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/menu")
                            .content(objectMapper.writeValueAsString(expected))
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().is(409));

            // Then
            Mockito.verify(menuService, Mockito.times(2)).addMenu(expected, "path");
        }
    }

    @Test
    void updateMenu() throws Exception {
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "수정되었습니다.";
            MenuUpdateDto expected = new MenuUpdateDto(
                    name,
                    cost,
                    info,
                    details,
                    usePlanner
            );
            Mockito.doNothing().when(menuService).updateMenu(expected, null, testId, uploadDir);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/update")
                            .content(objectMapper.writeValueAsString(expected))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).updateMenu(expected, null, testId, uploadDir);
        }

        // 2. 중복 상품명 등록
        {
            // Given
            String expectedMsg = "데이터 중복입니다.";
            MenuUpdateDto expected = new MenuUpdateDto(
                    name,
                    cost,
                    info,
                    details,
                    usePlanner
            );
            Mockito.doThrow(new SameNameException(expectedMsg)).when(menuService).updateMenu(expected,  null, testId, uploadDir);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/update")
                            .content(objectMapper.writeValueAsString(expected))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(409, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(2)).updateMenu(expected, null, testId, uploadDir);
        }
    }

    @Test
    void updateMenuInfo() throws Exception{
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "수정되었습니다.";
            MenuUpdateDto expected = new MenuUpdateDto(
                    name,
                    cost,
                    info,
                    details,
                    usePlanner
            );
            Mockito.doNothing().when(menuService).updateMenu(expected, null, testId, uploadDir);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/info/update")
                            .content(objectMapper.writeValueAsString(expected))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).updateMenu(expected, null, testId, uploadDir);
        }
    }

    @Test
    void deleteMenu() throws Exception{
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "삭제되었습니다.";
            Mockito.doNothing().when(menuService).deleteMenu(testId);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/manager/menu/1/del")).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).deleteMenu(testId);
        }

        // 2. 없는 id 요청
        {
            // Given
            String expectedMsg = "잘못된 id 입니다.";
            Mockito.doThrow(new CantFindByIdException(1L)).when(menuService).deleteMenu(testId);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/manager/menu/1/del")).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(500, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(2)).deleteMenu(testId);
        }
    }

    @Test
    void setSoldOut() throws Exception{
        // 1. 정상 요청 - 품절 처리
        {
            // Given
            String expectedMsg = "반영되었습니다.";
            String type = "y";
            Mockito.doNothing().when(menuService).setSoldOut(testId, type);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/sold/" + type)).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).setSoldOut(testId, type);
        }

        // 2. 정상 요청 - 품절 처리 취소
        {
            // Given
            String expectedMsg = "반영되었습니다.";
            String type = "n";
            Mockito.doNothing().when(menuService).setSoldOut(testId, type);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/sold/" + type)).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).setSoldOut(testId, type);
        }

        // 3. 없는 id 요청
        {
            // Given
            String expectedMsg = "잘못된 id 입니다.";
            String type = "y";
            Mockito.doThrow(new CantFindByIdException(expectedMsg, new IllegalArgumentException())).when(menuService).setSoldOut(testId, type);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/sold/" + type)).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(500, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(2)).setSoldOut(testId, type);
        }

        // 4. 잘못된 파라미터 요청
        {
            // Given
            String expectedMsg = "잘못된 파라미터입니다.";
            String type = "z";
            Mockito.doThrow(new WrongParameter(expectedMsg)).when(menuService).setSoldOut(testId, type);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/manager/menu/1/sold/" + type)).andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(400, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).setSoldOut(testId, type);
        }
    }
}