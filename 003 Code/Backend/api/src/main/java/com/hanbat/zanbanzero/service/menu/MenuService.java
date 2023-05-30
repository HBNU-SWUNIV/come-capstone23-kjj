package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.dto.menu.MenuManagerInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import lombok.RequiredArgsConstructor;
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
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final MenuInfoRepository menuInfoRepository;

    @Cacheable(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public List<MenuDto> getMenus() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map((menu) -> MenuDto.of(menu))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "MenuInfoDto", key = "#id", cacheManager = "cacheManager")
    public MenuInfoDto getMenuInfo(Long id) throws CantFindByIdException {
        MenuInfo menu = menuInfoRepository.findByIdAndFetch(id).orElseThrow(CantFindByIdException::new);

        return MenuInfoDto.of(menu);
    }

    @Transactional
    public List<MenuManagerInfoDto> getMenusForManager() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuInfo> menuInfos = menuInfoRepository.findAll();

        List<MenuManagerInfoDto> result = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) result.add(MenuManagerInfoDto.of(menus.get(i), menuInfos.get(i)));

        return result;
    }

    public Boolean isPlanner() { return menuRepository.existsByUsePlannerTrue(); }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void addMenu(MenuUpdateDto dto, String filePath) throws SameNameException {
        if (menuRepository.existsByName(dto.getName()) || (menuRepository.existsByUsePlannerTrue() && dto.getUsePlanner())) throw new SameNameException("데이터 중복입니다.");

        Menu menu = menuRepository.save(Menu.of(dto, filePath));
        menuInfoRepository.save(dto.of(menu));
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void updateMenu(MenuUpdateDto dto, MultipartFile file, Long id) throws CantFindByIdException, IOException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        MenuInfo menuInfo = menuInfoRepository.findById(id).orElseThrow(CantFindByIdException::new);

        if (file != null) {
            if (menu.getImage() == null) menu.setImage(menuImageService.uploadImage(file));
            else menuImageService.updateImage(file, menu.getImage());
        }

        menu.patch(dto);
        menuInfo.patch(dto);
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = "1", cacheManager = "cacheManager")
    public void deleteMenu(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        List<UserPolicy> policies = userPolicyRepository.findAllByDefaultMenu(id);
        for (UserPolicy policy : policies) policy.setDefaultMenu(null);

        menuRepository.delete(menu);
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

    @Transactional
    public void setPlanner(Long id) throws CantFindByIdException, WrongParameter {
        if (menuRepository.existsByUsePlannerTrue()) throw new WrongParameter("이미 식단표를 사용하고 있습니다.");

        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        menu.setUsePlanner(true);
    }

    @Transactional
    public void changePlanner(Long id) throws CantFindByIdException {
        Menu old = menuRepository.findByUsePlanner(true);
        if (old != null) old.setUsePlanner(false);

        Menu n = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        n.setUsePlanner(true);
    }
}
