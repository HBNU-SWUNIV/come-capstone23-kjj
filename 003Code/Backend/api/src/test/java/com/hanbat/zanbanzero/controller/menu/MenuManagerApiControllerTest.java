package com.hanbat.zanbanzero.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.service.image.ImageService;
import com.hanbat.zanbanzero.service.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hanbat.zanbanzero.util.MenuTestTemplate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(MenuManagerApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuManagerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MenuService menuService;

    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("[MENU_MANAGER] 관리자 전용 전체 메뉴 조회 API")
    void getMenusForManager() throws Exception{
        // 1. 정상 시나리오
        {
            // Given
            List<MenuManagerInfoDto> expected = new ArrayList<>();
            when(menuService.getMenusForManager()).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/menu"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).getMenusForManager();
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식단표 사용 메뉴 유무 조회 API")
    void isPlanned() throws Exception{
        // 1. 식단표 사용중
        {
            // Given
            when(menuService.isPlanned()).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/menu/planner"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("true", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(1)).isPlanned();
        }
        // 2. 식단표 미사용
        {
            // Given
            when(menuService.isPlanned()).thenReturn(false);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/menu/planner"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("false", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(2)).isPlanned();
        }
    }
    
    @Test
    @DisplayName("[MENU_MANAGER] 식단표 사용 설정 API")
    void setPlanner() throws Exception {
        // 1. 정상 시나리오
        {
            // Given
            when(menuService.setPlanner(testMenuId())).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/manager/menu/%s/planner", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).setPlanner(testMenuId());
        }
        // 2. 이미 식단표를 사용하는 메뉴가 있는 경우
        {
            // Given
            when(menuService.setPlanner(testMenuId())).thenReturn(false);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/manager/menu/%s/planner", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(2)).setPlanner(testMenuId());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식단표 교체 설정 API")
    void changePlanner() throws Exception {
        // 1. 정상 시나리오
        {
            // Given
            when(menuService.changePlanner(testMenuId())).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/change/planner", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).changePlanner(testMenuId());
        }
        // 2. menuId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuService.changePlanner(testMenuId())).thenThrow(new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, testMenuId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/change/planner", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).changePlanner(testMenuId());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴 추가 API")
    void addMenu() throws Exception {

        // Given
        MenuUpdateDto target = new MenuUpdateDto(
                testMenuName(),
                testCost(),
                testInfo(),
                testDetails(),
                testUsePlannerFalse()
        );
        MenuDto expected = new MenuDto(
                null,
                testMenuName(),
                testCost(),
                testImage(),
                testSold()
        );
        MockPart data = new MockPart("data", objectMapper.writeValueAsBytes(target));
        data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 1. 정상 시나리오
        {
            // Given
            MockMultipartFile multipartFile = testMultipartFile();
            when(menuService.addMenu(target, null)).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/manager/menu")
                            .file(multipartFile)
                            .part(data))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).addMenu(target, null);
        }

        // 2. 중복 상품명 등록
        {
            // Given
            Mockito.doThrow(new SameNameException("""
                이미 해당 이름을 가진 메뉴가 존재합니다.
                dto : """)).when(menuService).addMenu(target, null);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/manager/menu")
                            .part(data))
                    .andExpect(MockMvcResultMatchers.status().is(409));
            verify(menuService, times(2)).addMenu(target, null);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴에 식재료 정보 등록 API")
    void setFood() throws Exception{
        // 1. 정상 시나리오
        {
            // Given
            when(menuService.setFood(testMenuId(), testFoodId())).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/food/%s", testMenuId(), testFoodId())))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("true", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(1)).setFood(testMenuId(), testFoodId());
        }
        // 2. menuId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuService.setFood(testMenuId(), testFoodId())).thenThrow(new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, testMenuId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/food/%s", testMenuId(), testFoodId())))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).setFood(testMenuId(), testFoodId());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 전체 식재료 정보 조회 API")
    void getFood() throws Exception{
        // 1. 정상 시나리오
        {
            // Given
            List<MenuFoodDto> menuFoodDtos = new ArrayList<>();
            MenuFoodDtos expected = new MenuFoodDtos(menuFoodDtos);
            when(menuService.getFood()).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/menu/food"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).getFood();
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 특정 식재료 정보 조회 API")
    void getOneFood() throws Exception{
        // 1. 정상 시나리오
        {
            // Given
            Map<String, Integer> testFood = new HashMap<>();
            MenuFoodDto expected = new MenuFoodDto(
                    testFoodId(),
                    testMenuName(),
                    testFood
            );
            when(menuService.getOneFood(testFoodId())).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/manager/menu/food/%s", testFoodId())))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).getOneFood(testFoodId());
        }
        // 2. foodId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuService.getOneFood(testFoodId())).thenThrow(new CantFindByIdException("""
                해당 id를 가진 식재료 데이터를 찾을 수 없습니다.
                menuId : """, testFoodId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/manager/menu/food/%s", testFoodId())))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).getOneFood(testFoodId());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식재료 정보 추가 API")
    void addFood() throws Exception{
        // Given
        Map<String, Integer> testFoodData = new HashMap<>();

        // 1. 정상 시나리오
        {
            // Given
            when(menuService.addFood(testFoodName(), testFoodData)).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/manager/menu/food")
                            .param("name", testFoodName())
                            .content(objectMapper.writeValueAsBytes(testFoodData))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("true", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(1)).addFood(testFoodName(), testFoodData);
        }
        // 2. name이 중복되는 경우
        {
            // Given
            when(menuService.addFood(testFoodName(), testFoodData)).thenReturn(false);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/manager/menu/food")
                            .param("name", testFoodName())
                            .content(objectMapper.writeValueAsBytes(testFoodData))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("false", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(2)).addFood(testFoodName(), testFoodData);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴 수정 API")
    void updateMenu() throws Exception {
        // Given
        MenuUpdateDto target = new MenuUpdateDto(
                testMenuName(),
                testCost(),
                testInfo(),
                testDetails(),
                testUsePlannerFalse()
        );

        MockMultipartFile multipartFile = testMultipartFile();
        MockPart mockPartNameData = new MockPart("data", objectMapper.writeValueAsBytes(target));
        mockPartNameData.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MenuInfoDto expected = new MenuInfoDto(
                testMenuId(),
                testMenuName(),
                testCost(),
                testImage(),
                testSold(),
                testInfo(),
                testDetails()
        );

        // 1. 정상 시나리오
        {
            // Given
            when(menuService.updateMenu(target, multipartFile, testMenuId(), testFilePath())).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart(HttpMethod.PATCH, String.format("/api/manager/menu/%s", testMenuId()))
                            .file(multipartFile)
                            .part(mockPartNameData))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).updateMenu(target, multipartFile, testMenuId(), testFilePath());
        }

        // 2. 중복 상품명 등록
        {
            // Given
            when(menuService.updateMenu(target, multipartFile, testMenuId(), testFilePath()))
                    .thenThrow(new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, testMenuId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart(HttpMethod.PATCH, String.format("/api/manager/menu/%s", testMenuId()))
                            .file(multipartFile)
                            .part(mockPartNameData))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).updateMenu(target, multipartFile, testMenuId(), testFilePath());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴 삭제 API")
    void deleteMenu() throws Exception{
        // Given
        MenuDto expected = new MenuDto(
                testMenuId(),
                testMenuName(),
                testCost(),
                testImage(),
                testSold()
        );

        // 1. 정상 요청
        {
            // Given
            when(menuService.deleteMenu(testMenuId())).thenReturn(expected);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/manager/menu/%s", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(menuService, times(1)).deleteMenu(testMenuId());
        }

        // 2. menuId 데이터가 없는 경우
        {
            // Given
            when(menuService.deleteMenu(testMenuId()))
                    .thenThrow(new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, testMenuId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/manager/menu/%s", testMenuId())))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).deleteMenu(testMenuId());
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 품절 설정 API")
    void setSoldOut() throws Exception{
        // 1. 정상 요청 - 품절 처리
        {
            // Given
            String type = "y";
            when(menuService.setSoldOut(testMenuId(), type)).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/sold/%s", testMenuId(), type)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("true", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(1)).setSoldOut(testMenuId(), type);
        }

        // 2. 정상 요청 - 재판매 처리
        {
            // Given
            String type = "n";
            when(menuService.setSoldOut(testMenuId(), type)).thenReturn(true);

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/sold/%s", testMenuId(), type)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        assertEquals("true", result.getResponse().getContentAsString());
                    });
            verify(menuService, times(1)).setSoldOut(testMenuId(), type);
        }
        
        // 3. menuId 데이터가 없는 경우
        {
            // Given
            String type = "y";
            when(menuService.setSoldOut(testMenuId(), type))
                    .thenThrow(new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, testMenuId()));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/sold/%s", testMenuId(), type)))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(2)).setSoldOut(testMenuId(), type);
        }

        // 4. 잘못된 파라미터가 전달된 경우
        {
            // Given
            String type = "s";
            when(menuService.setSoldOut(testMenuId(), type))
                    .thenThrow(new WrongParameter("""
                    잘못된 타입입니다. (y || n)
                    type : """, type));

            // When & Then
            mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/manager/menu/%s/sold/%s", testMenuId(), type)))
                    .andExpect(MockMvcResultMatchers.status().is(400));
            verify(menuService, times(1)).setSoldOut(testMenuId(), type);
        }
    }
}