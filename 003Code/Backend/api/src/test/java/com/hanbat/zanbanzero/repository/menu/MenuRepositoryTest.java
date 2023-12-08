package com.hanbat.zanbanzero.repository.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.hanbat.zanbanzero.util.MenuTestTemplate.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("[MENU] 데이터 저장")
    void save() {
        // Given
        Menu menu = testMenu(null,null, null, testUsePlannerFalse());

        // When
        Menu saved = menuRepository.save(menu);

        // Then
        assertEquals(menu, saved);
    }

    @Test
    @DisplayName("[MENU] 이름 중복 확인")
    void existsByName() {
        // Given
        Menu menu = testMenu(null,null, null, testUsePlannerFalse());
        menuRepository.save(menu);

        // 1. 존재하지 않는 경우
        {
            // Given
            String notExistMenuName = "notExist";

            // When
            Boolean result = menuRepository.existsByName(notExistMenuName);

            // When
            assertFalse(result);
        }

        // 2. 존재하는 경우
        {
            // When
            Boolean result = menuRepository.existsByName(menu.getName());

            // Then
            assertTrue(result);
        }
    }

    @Test
    @Sql(value ="classpath:testInit.sql")
    @DisplayName("[MENU] 전체 메뉴 조회(left join MenuInfo & MenuFood)")
    void findAllWithMenuInfoAndMenuFood() {
        // Given
        Menu noMenuInfoMenu = testMenu(null, null, testMenuFood(null), testUsePlannerFalse());
        Menu noMenuFoodMenu = testMenu(null, testMenuInfo(null), null, testUsePlannerFalse());
        menuRepository.saveAll(List.of(noMenuInfoMenu, noMenuFoodMenu));

        // When
        List<Menu> result = menuRepository.findAllWithMenuInfoAndMenuFood();

        // Then
        assertEquals(3, result.size());
        assertFalse(result.get(0).getMenuFood() instanceof HibernateProxy);
        assertFalse(result.get(0).getMenuInfo() instanceof HibernateProxy);
    }

    @Test
    @DisplayName("[MENU] 식단표 사용중인 메뉴 조회")
    void findByUsePlannerTrue() {
        // 1. 존재하지 않는 경우
        {
            // Given
            Menu menu = testMenu(null,null, null, testUsePlannerFalse());
            menuRepository.save(menu);

            // When
            Optional<Menu> result = menuRepository.findByUsePlannerTrue();

            // Then
            assertTrue(result.isEmpty());
        }

        // 2. 존재하는 경우
        {
            // Given
            Menu menu = testMenu(null, null, null, testUsePlannerTrue());
            menuRepository.save(menu);

            // When
            Optional<Menu> result = menuRepository.findByUsePlannerTrue();

            // Then
            assertTrue(result.isPresent());
            assertEquals(menu.getName(), result.get().getName());
        }
    }

    @Test
    @DisplayName("[MENU] 식단표 사용중인 메뉴 존재 여부 조회")
    void existsByUsePlannerTrue() {
        // 1. 존재하지 않는 경우
        {
            // Given
            Menu menu = testMenu(null,null, null, testUsePlannerFalse());
            menuRepository.save(menu);

            // When
            Boolean result = menuRepository.existsByUsePlannerTrue();

            // Then
            assertFalse(result);
        }

        // 2. 존재하는 경우
        {
            // Given
            Menu menu = testMenu(null, null, null, testUsePlannerTrue());
            menuRepository.save(menu);

            // When
            Boolean result = menuRepository.existsByUsePlannerTrue();

            // Then
            assertTrue(result);
        }
    }

    @Test
    @Sql(value = "classpath:testInit.sql")
    @DisplayName("[MENU] id로 조회(left join MenuInfo")
    void findByIdWithMenuInfo() {
        // Given
        Long testId = 1L;

        // When
        Menu result = menuRepository.findByIdWithMenuInfo(testId).orElseThrow();

        // Then
        assertEquals(testMenuName(), result.getName());
        assertFalse(result.getMenuInfo() instanceof HibernateProxy);
        assertTrue(result.getMenuFood() instanceof HibernateProxy);
    }
}