package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.service.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static com.hanbat.zanbanzero.util.MenuTestTemplate.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplV1Test {

    @InjectMocks
    private MenuServiceImplV1 menuService;

    @Mock
    private ImageService imageService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuInfoRepository menuInfoRepository;
    @Mock
    private MenuFoodRepository menuFoodRepository;
    
    @Test
    @DisplayName("[MENU_USER] 전체 메뉴 조회")
    void getMenus() {
        // 1. 정상 시나리오
        {
            // Given
            List<MenuUserInfoDto> dtos = new ArrayList<>();
            MenuUserInfoDtos expected = new MenuUserInfoDtos(dtos);

            // When
            MenuUserInfoDtos result = menuService.getMenus();

            // Then
            assertEquals(expected, result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 전체 메뉴 조회")
    void getMenusForManager() {
        // 1. 정상 시나리오
        {
            // Given
            List<MenuManagerInfoDto> expected = new ArrayList<>();

            // When
            List<MenuManagerInfoDto> result = menuService.getMenusForManager();

            // Then
            assertEquals(expected, result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식단표 사용 메뉴 유무 조회")
    void isPlanned() {
        // 1. 식단표를 사용중인 메뉴가 없는 경우
        {
            // Given
            Boolean expected = false;

            // When
            Boolean result = menuService.isPlanned();

            // Then
            assertEquals(expected, result);
        }

        // 2. 식단표를 사용중인 메뉴가 존재하는 경우
        {
            // Given
            Boolean expected = true;
            when(menuRepository.existsByUsePlannerTrue()).thenReturn(true);

            // When
            Boolean result = menuService.isPlanned();

            // Then
            assertEquals(expected, result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴 추가")
    void addMenu() throws Exception{
        // Given
        MenuInfo menuInfo = testMenuInfo(null);
        Menu menu = testMenu(null, menuInfo, null, testUsePlannerFalse());
        MenuUpdateDto menuUpdateDto = new MenuUpdateDto(
                testMenuName(),
                testCost(),
                testInfo(),
                testDetails(),
                testUsePlannerFalse()
        );

        // 1. 정상 시나리오
        {
            // Given
            MenuDto expected = MenuDto.from(menu);
            when(menuInfoRepository.save(any())).thenReturn(menuInfo);
            when(menuRepository.save(any())).thenReturn(menu);

            // When
            MenuDto result = menuService.addMenu(menuUpdateDto, testFilePath());

            // Then
            assertEquals(expected, result);
        }

        // 2. 같은 이름을 가진 메뉴가 이미 존재하는 경우
        {
            // Given
            when(menuRepository.existsByName(testMenuName())).thenReturn(true);

            // When & Then
            assertThrows(SameNameException.class, () -> {
                menuService.addMenu(menuUpdateDto, testFilePath());
            });
        }

        // 3. 이미 식단표를 사용중인 메뉴가 있는 상태로 식단표 사용 메뉴를 추가하는 경우
        {
            // Given
            MenuUpdateDto usePlannerMenuUpdateDto = new MenuUpdateDto(
                    testMenuName(),
                    testCost(),
                    testInfo(),
                    testDetails(),
                    testUsePlannerTrue()
            );

            // When & Then
            assertThrows(SameNameException.class, () -> {
                menuService.addMenu(usePlannerMenuUpdateDto, testFilePath());
            });
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴에 식재료 정보 등록")
    void setFood() throws Exception{
        // Given
        Menu menu = testMenu(null,null, testMenuFood(testFoodId()), testUsePlannerFalse());
        // 1. 정상 시나리오 - 식재료 id가 0이 아닌 경우
        {
            // Given
            Long testMenuFoodId = 2L;
            MenuFood expected = testMenuFood(testMenuFoodId);
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.of(menu));
            when(menuFoodRepository.getReferenceById(any())).thenReturn(expected);

            // When
            Boolean result = menuService.setFood(testMenuId(), expected.getId());

            // Then
            assertEquals(menu.getMenuFood().getId(), expected.getId());
            assertEquals(true, result);
        }
        
        // 2. 정상 시나리오 - 식재료 id가 0인 경우
        {
            // Given
            Long clearFoodId = 0L;

            // When
            Boolean result = menuService.setFood(testMenuId(), clearFoodId);

            // Then
            assertNull(menu.getMenuFood());
            assertEquals(true, result);
        }
        
        // 3. 메뉴 id 데이터가 없는 경우
        {
            // Given
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.setFood(testMenuId(), testFoodId());
            });
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 전체 식재료 정보 조회")
    void getFood() {
        // 1. 정상 시나리오
        {
            // Given
            List<MenuFood> menuFoods = new ArrayList<>();
            when(menuFoodRepository.findAll()).thenReturn(menuFoods);
            MenuFoodDtos expected = new MenuFoodDtos(
                    menuFoods.stream()
                            .map(MenuFoodDto::from)
                            .toList());

            // When
            MenuFoodDtos result = menuService.getFood();

            // Then
            assertEquals(expected, result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 메뉴 수정")
    void updateMenu() throws Exception{
        MenuUpdateDto menuUpdateDto = new MenuUpdateDto(
                "new name",
                5000,
                "new info",
                "new details",
                testUsePlannerFalse()
        );

        MockMultipartFile multipartFile = testMultipartFile();

        // 1. 정상 시나리오 - file 존재, menu.getImage() == null
        {
            // Given
            Menu menu = testMenu(null, testMenuInfo(null), null, testUsePlannerFalse());
            menu.setImage(null);
            when(menuRepository.findByIdWithMenuInfo(testMenuId())).thenReturn(Optional.of(menu));
            when(imageService.uploadImage(any(), any())).thenReturn(testImage());

            // When
            MenuInfoDto result = menuService.updateMenu(menuUpdateDto, multipartFile, testMenuId(), testFilePath());

            // Then
            assertEquals(result.image(), testImage());
            assertEquals(result.name(), menuUpdateDto.name());
            assertEquals(result.cost(), menuUpdateDto.cost());
            assertEquals(result.info(), menuUpdateDto.info());
            assertEquals(result.details(), menuUpdateDto.details());
        }

        // 2. 정상 시나리오 - file 존재, menu.getImage() != null
        {
            // Given
            doNothing().when(imageService).updateImage(any(), any());

            // When
            MenuInfoDto result = menuService.updateMenu(menuUpdateDto, multipartFile, testMenuId(), testFilePath());

            // Then
            assertEquals(result.image(), testImage());
            assertEquals(result.name(), menuUpdateDto.name());
            assertEquals(result.cost(), menuUpdateDto.cost());
            assertEquals(result.info(), menuUpdateDto.info());
            assertEquals(result.details(), menuUpdateDto.details());
        }

        // 3. menuId 데이터가 없는 경우
        {
            // Given
            when(menuRepository.findByIdWithMenuInfo(testMenuId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.updateMenu(menuUpdateDto, multipartFile, testMenuId(), testFilePath());
            });
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 품절 설정")
    void setSoldOut() throws Exception{
        Menu menu = testMenu(null,null, null, testUsePlannerFalse());
        // 1. 정상 시나리오 - type = 'y'
        {
            // Given
            String type = "y";
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.of(menu));

            // When
            Boolean result = menuService.setSoldOut(testMenuId(), type);

            // Then
            assertTrue(result);
            assertTrue(menu.getSold());
        }

        // 2. 정상 시나리오 - type = 'n'
        {
            // Given
            String type = "n";

            // When
            Boolean result = menuService.setSoldOut(testMenuId(), type);

            // Then
            assertTrue(result);
            assertFalse(menu.getSold());
        }

        // 3. 잘못된 type이 전달된 경우
        {
            // Given
            String type = "s";

            // When & Then
            assertThrows(WrongParameter.class, () -> {
                menuService.setSoldOut(testMenuId(), type);
            });
        }

        // 4. menuId 데이터를 찾을 수 없는 경우
        {
            // Given
            String type = "y";
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.setSoldOut(testMenuId(), type);
            });
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식단표 사용 설정")
    void setPlanner() throws Exception{
        Menu menu = testMenu(null,null, null, testUsePlannerFalse());
        // 1. 정상 시나리오
        {
            // Given
            when(menuRepository.existsByUsePlannerTrue()).thenReturn(false);
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.of(menu));
            
            // When
            Boolean result = menuService.setPlanner(testMenuId());
            
            // Then
            assertTrue(result);
            assertTrue(menu.isUsePlanner());
        }
        
        // 2. menuId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.setPlanner(testMenuId());
            });
        }

        // 3. 이미 식단표를 사용중인 메뉴가 있는 경우
        {
            // Given
            when(menuRepository.existsByUsePlannerTrue()).thenReturn(true);

            // When
            Boolean result = menuService.setPlanner(testMenuId());

            // Then
            assertFalse(result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식단표 교체 설정")
    void changePlanner() throws Exception{
        // 1. 정상 시나리오 - 이전에 식단표를 사용하던 메뉴가 없는 경우
        {
            // Given
            Menu menu = testMenu(null,null, null, testUsePlannerFalse());
            when(menuRepository.findByUsePlanner(true)).thenReturn(Optional.empty());
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.of(menu));

            // When
            Boolean result = menuService.changePlanner(testMenuId());

            // Then
            assertTrue(result);
            assertTrue(menu.isUsePlanner());
        }

        // 2. 정상 시나리오 - 이전에 식단표를 사용하던 메뉴가 있는 경우
        {
            // Given
            Menu beforeUsePlannerMenu = testMenu(null,null, null, testUsePlannerTrue());
            Menu newUsePlannerMenu = testMenu(null,null, null, testUsePlannerFalse());
            when(menuRepository.findByUsePlanner(true)).thenReturn(Optional.of(beforeUsePlannerMenu));
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.of(newUsePlannerMenu));

            // When
            Boolean result = menuService.changePlanner(testMenuId());

            // Then
            assertTrue(result);
            assertFalse(beforeUsePlannerMenu.isUsePlanner());
            assertTrue(newUsePlannerMenu.isUsePlanner());
        }

        // 3. menuId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuRepository.findById(testMenuId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.changePlanner(testMenuId());
            });
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 식재료 추가")
    void addFood() throws Exception{
        // Given
        Map<String, Integer> testFoodData = new HashMap<>();

        // 1. 정상 시나리오 - 중복되는 이름이 없는 경우
        {
            // Given
            when(menuFoodRepository.existsByName(testFoodName())).thenReturn(false);

            // When
            Boolean result = menuService.addFood(testFoodName(), testFoodData);

            // Then
            assertTrue(result);
        }

        // 2. 정상 시나리오 - 중복되는 이름이 있는 경우
        {
            // Given
            when(menuFoodRepository.existsByName(testFoodName())).thenReturn(true);

            // When
            Boolean result = menuService.addFood(testFoodName(), testFoodData);

            // Then
            assertFalse(result);
        }
    }

    @Test
    @DisplayName("[MENU_MANAGER] 특정 식재료 정보 조회")
    void getOneFood() throws Exception{
        // 1. 정상 시나리오
        {
            // Given
            MenuFood menuFood = testMenuFood(null);
            when(menuFoodRepository.findById(testFoodId())).thenReturn(Optional.of(menuFood));

            // When
            MenuFoodDto result = menuService.getOneFood(testFoodId());

            // Then
            assertEquals(MenuFoodDto.from(menuFood), result);
        }

        // 2. foodId 데이터를 찾을 수 없는 경우
        {
            // Given
            when(menuFoodRepository.findById(testFoodId())).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CantFindByIdException.class, () -> {
                menuService.getOneFood(testFoodId());
            });
        }
    }
}