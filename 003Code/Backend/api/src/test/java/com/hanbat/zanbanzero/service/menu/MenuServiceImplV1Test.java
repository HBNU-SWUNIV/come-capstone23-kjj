package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(MenuService.class)
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuRepository menuRepository;

    private String name = "치킨너겟";

    @BeforeEach
    void setup() {
        final Menu menu = new Menu(null, null, null, name, 2000, "imageURI", true, false);
        menuRepository.save(menu);
    }
    
    @Test
    @DisplayName("[MENU_USER] 전체 메뉴 조회")
    void getMenus() {
        
    }
}