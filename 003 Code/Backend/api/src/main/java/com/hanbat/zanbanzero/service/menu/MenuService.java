package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.dto.menu.MenuManagerInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuImageService menuImageService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final MenuInfoRepository menuInfoRepository;
    private final CacheManager cacheManager;
    private final Long storeId = 1L;

    @Cacheable(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public List<MenuDto> getMenus() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map((menu) -> MenuDto.createMenuDto(menu))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "MenuInfoDto", key = "#id", cacheManager = "cacheManager")
    public MenuInfoDto getMenuInfo(Long id) throws CantFindByIdException {
        MenuInfo menu = menuInfoRepository.findByIdAndFetch(id).orElseThrow(CantFindByIdException::new);

        MenuInfoDto result = MenuInfoDto.createMenuDto(menu);
        return result;
    }

    public List<MenuManagerInfoDto> getMenusForManager() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuInfo> menuInfos = menuInfoRepository.findAll();

        List<MenuManagerInfoDto> result = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            result.add(MenuManagerInfoDto.createMenuManagerInfoDto(menus.get(i), menuInfos.get(i)));
        }

        return result;
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void addMenu(MenuUpdateDto dto, String filePath) throws SameNameException {
        if (menuRepository.existsByName(dto.getName())) {
            throw new SameNameException("데이터 중복입니다.");
        }

        Menu menu = menuRepository.save(Menu.createMenu(dto, storeRepository.getReferenceById(storeId), filePath));

        menuInfoRepository.save(dto.createMenuInfo(menu));
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void updateMenu(MenuUpdateDto dto, MultipartFile file, Long id) throws CantFindByIdException, IOException {
        if (menuRepository.existsByName(dto.getName())) {
            throw new SameNameException("데이터 중복입니다.");
        }
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        MenuInfo menuInfo = menuInfoRepository.findById(id).orElseThrow(CantFindByIdException::new);

        if (file != null) {
            if (menu.getImage() == null) {
                menu.setImage(menuImageService.uploadImage(file));
            }
            else menuImageService.updateImage(file, menu.getImage());
        }

        menu.patch(dto);
        menuInfo.patch(dto);
    }

    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void deleteMenu(Long id) throws CantFindByIdException {
        MenuInfo menu = menuInfoRepository.findById(id).orElseThrow(CantFindByIdException::new);

        menuInfoRepository.delete(menu);
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void setSoldOut(Long id, char type) throws CantFindByIdException, WrongParameter {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        switch (type) {
            case 'n':
                menu.setSold(false);
                break;
            case 'y':
                menu.setSold(true);
                break;
            default:
                throw new WrongParameter("잘못된 파라미터입니다.");
        }
    }

}
