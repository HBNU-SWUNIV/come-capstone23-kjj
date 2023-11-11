package com.hanbat.zanbanzero.controller.menu;

import com.hanbat.zanbanzero.controller.ControllerTestClass;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuUserInfoDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.menu.MenuService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(MenuManagerApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuManagerApiControllerTest extends ControllerTestClass {

    @MockBean
    MenuService menuService;

    private final Long testId = 1L;
    private final String uploadDir = "img/menu";

    @Test
    void getMenus() throws Exception{
        // 1. 정상 요청
        {
            // Given
            List<MenuUserInfoDto> expected = new ArrayList<>();
            Mockito.when(menuService.getMenus()).thenReturn(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/menu")).andReturn();

            // Then
            assertEquals(objectMapper.writeValueAsString(expected), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).getMenus();
        }
    }

    @Test
    void getMenuInfo() throws Exception{
        // 1. 정상 요청
        {
            // Given
            MenuInfoDto expected = new MenuInfoDto();
//            Mockito.when(menuService.getMenuInfo(testId)).thenReturn(expected);

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/menu/1")).andReturn();

            // Then
            assertEquals(objectMapper.writeValueAsString(expected), result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

//            Mockito.verify(menuService, Mockito.times(1)).getMenuInfo(testId);
        }
    }

    @Test
    void addMenu() throws Exception {
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "등록되었습니다.";
            MenuUpdateDto expected = new MenuUpdateDto();
            Mockito.doNothing().when(menuService).addMenu(expected, "path");

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/menu/add")
                    .content(objectMapper.writeValueAsString(expected))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(1)).addMenu(expected, "path");
        }

        // 2. 중복 상품명 등록
        {
            // Given
            String expectedMsg = "데이터 중복입니다.";
            MenuUpdateDto expected = new MenuUpdateDto();
            Mockito.doThrow(new SameNameException(expectedMsg)).when(menuService).addMenu(expected, "path");

            // When
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/menu/add")
                            .content(objectMapper.writeValueAsString(expected))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // Then
            assertEquals(expectedMsg, result.getResolvedException().getMessage());
            assertEquals(409, result.getResponse().getStatus());

            Mockito.verify(menuService, Mockito.times(2)).addMenu(expected, "path");
        }
    }

    @Test
    void updateMenu() throws Exception {
        // 1. 정상 요청
        {
            // Given
            String expectedMsg = "수정되었습니다.";
            MenuUpdateDto expected = new MenuUpdateDto();
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
            MenuUpdateDto expected = new MenuUpdateDto();
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
            MenuUpdateDto expected = new MenuUpdateDto();
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