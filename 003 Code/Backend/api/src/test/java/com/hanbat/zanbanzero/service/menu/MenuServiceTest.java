package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuRepository menuRepository;

    private String name = "치킨너겟";

    @BeforeEach
    void setup() {
        final Menu menu = new Menu(null, name, 2000, "두유", null);
        menuRepository.save(menu);
    }
}