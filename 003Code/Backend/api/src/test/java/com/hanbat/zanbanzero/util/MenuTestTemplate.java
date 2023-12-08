package com.hanbat.zanbanzero.util;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import org.springframework.mock.web.MockMultipartFile;

public class MenuTestTemplate {
    public static Menu testMenu(Long testId, MenuInfo menuInfo, MenuFood menuFood, Boolean usePlanner) {
        return new Menu(
                testId,
                menuInfo,
                menuFood,
                testMenuName(),
                testCost(),
                testImage(),
                testSold(),
                usePlanner
        );
    }
    public static MenuInfo testMenuInfo(Long testId) {
        return new MenuInfo(
                testId,
                testInfo(),
                testDetails()
        );
    }
    public static MenuFood testMenuFood(Long testId) {
        return new MenuFood(
                testId,
                null,
                testFoodName(),
                testFood()
        );
    }
    public static MockMultipartFile testMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test.png",
                "multipart/form-data",
                "test".getBytes());
    }
    public static Long testMenuId() {
        return 1L;
    }
    public static Long testFoodId() {
        return  1L; 
    }
    public static String testMenuName() {
        return "test Menu name";
    }
    public static String testFoodName() {
        return "test Food name";
    }
    public static int testCost() {
        return 3000;
    }
    public static String testInfo() {
        return "test info";
    }
    public static String testDetails() {
        return "test details";
    }
    public static boolean testUsePlannerTrue() {
        return true;
    }
    public static boolean testUsePlannerFalse() {
        return false;
    }
    public static String testImage() {
        return "image";
    }
    public static boolean testSold() {
        return true;
    }
    public static String testFilePath() {
        return "img/menu";
    }
    public static String testFood() { return "test Food"; }
}
