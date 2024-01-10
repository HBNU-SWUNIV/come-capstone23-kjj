package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService {

    MenuUserInfoDtos getMenus();

    List<MenuManagerInfoDto> getMenusForManager();

    Boolean isPlanned();

    MenuDto addMenu(MenuUpdateDto dto, String filePath) throws SameNameException;

    Boolean setFood(Long menuId, Long foodId) throws CantFindByIdException;

    MenuFoodDtos getFood();

    MenuInfoDto updateMenu(MenuUpdateDto dto, MultipartFile file, Long id, String uploadDir) throws CantFindByIdException, UploadFileException;

    MenuDto deleteMenu(Long id) throws CantFindByIdException;

    Boolean setSoldOut(Long id, String type) throws CantFindByIdException, WrongParameter;

    Boolean setPlanner(Long id) throws CantFindByIdException;

    Boolean changePlanner(Long id) throws CantFindByIdException;

    MenuFoodDto getOneFood(Long id) throws CantFindByIdException;
}
