package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.exception.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MenuService {
    MenuUserInfoDtos getMenus();

    MenuInfoDto getMenuInfo(Long id) throws CantFindByIdException;

    List<MenuManagerInfoDto> getMenusForManager();

    Boolean isPlanned();

    MenuDto addMenu(MenuUpdateDto dto, String filePath) throws SameNameException;

    MenuFoodDto addFood(Long id, String data) throws CantFindByIdException;

    Map<String, Integer> getFood(Long id) throws MapToStringException;

    Map<String, Integer> updateFood(Long id, Map<String, Integer> map) throws CantFindByIdException, MapToStringException;

    MenuInfoDto updateMenu(MenuUpdateDto dto, MultipartFile file, Long id, String uploadDir) throws CantFindByIdException, UploadFileException;

    MenuDto deleteMenu(Long id) throws CantFindByIdException;

    MenuDto setSoldOut(Long id, String type) throws CantFindByIdException, WrongParameter;

    MenuDto setPlanner(Long id) throws CantFindByIdException, WrongParameter;

    MenuDto changePlanner(Long id) throws CantFindByIdException;
}
