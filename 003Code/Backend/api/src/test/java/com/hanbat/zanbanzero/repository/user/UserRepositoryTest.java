package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired private MenuRepository menuRepository;

    private final String menuName = "test menu";

    private final MenuFood menuFood = new MenuFood(
            null,
            null,
            "test food",
            "{\"당근\":2,\"계란\":60,\"고구마\":200}"
    );

    private final MenuInfo menuInfo = new MenuInfo(null, "test menu info", "test menu details");
    private final Menu menu = new Menu(
            null,
            menuInfo,
            menuFood,
            menuName,
            3000,
            null,
            true,
            false
    );
    @Test
    @DisplayName("저장 및 ID로 Menu 조회")
    void saveAndFindUser() {
        // Given

        // When
        menuRepository.save(menu);

        // Then
        Optional<Menu> byId = menuRepository.findByName(menuName);
        assertTrue(byId.isPresent());
        assertEquals(menu.getName(), byId.get().getName());
    }

    @Test
    @DisplayName("Join fetch해서 조회")
    void findAllWithMenuInfo() {
        // Given

        // When
        menuRepository.save(menu);
        List<Menu> menus = menuRepository.findAllWithMenuInfoAndMenuFood();

        // Then
        assertFalse(menus.isEmpty());
    }
}