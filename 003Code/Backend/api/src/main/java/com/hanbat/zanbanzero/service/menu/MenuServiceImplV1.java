package com.hanbat.zanbanzero.service.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.order.OrderRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MenuServiceImplV1 implements MenuService{
    private final ImageService menuImageService;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final MenuInfoRepository menuInfoRepository;
    private final MenuFoodRepository menuFoodRepository;
    private final OrderRepository orderRepository;
    private static final String REDIS_CACHE_MANAGER = "redisCacheManager";

    private static final String ALL_MENUS_CACHE_KEY = "1";
    private static final String ALL_MENUS_CACHE_VALUE = "AllMenus";

    public static final String ALL_FOODS_CACHE_KEY = "2";
    public static final String ALL_FOODS_CACHE_VALUE = "AllFoods";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = ALL_MENUS_CACHE_VALUE, key = ALL_MENUS_CACHE_KEY, cacheManager = REDIS_CACHE_MANAGER)
    public MenuUserInfoDtos getMenus() {
        return MenuUserInfoDtos.from(menuRepository.findAllWithMenuInfoAndMenuFood().stream()
                .map(MenuUserInfoDto::from)
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuManagerInfoDto> getMenusForManager() {
        return menuRepository.findAllWithMenuInfoAndMenuFood().stream()
                .map(MenuManagerInfoDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isPlanned() { return menuRepository.existsByUsePlannerTrue(); }

    @Override
    @Transactional
    public MenuDto addMenu(MenuUpdateDto dto, String filePath) throws SameNameException {
        if (menuRepository.existsByName(dto.getName())) throw new SameNameException("""
                이미 해당 이름을 가진 메뉴가 존재합니다.
                dto : """ + dto);
        if (menuRepository.existsByUsePlannerTrue() && dto.getUsePlanner()) throw new SameNameException("""
                이미 식단을 사용중인 메뉴가 있는 상태입니다.
                식단을 미사용하거나, 기존에 식단을 사용하는 메뉴를 취소해주세요.
                dto : """ + dto);

        MenuInfo menuInfo = menuInfoRepository.save(dto.from());
        Menu menu = menuRepository.save(Menu.of(dto, menuInfo, filePath));
        return MenuDto.from(menu);
    }

    @Override
    @Transactional
    public void setFood(Long menuId, Long foodId) throws CantFindByIdException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, menuId));
        if (foodId == 0) menu.clearMenuFood();
        else menu.setMenuFood(menuFoodRepository.getReferenceById(foodId));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = ALL_FOODS_CACHE_VALUE, key = ALL_FOODS_CACHE_KEY, cacheManager = REDIS_CACHE_MANAGER)
    public MenuFoodDtos getFood() {
        return MenuFoodDtos.from(menuFoodRepository.findAll().stream()
                .map(MenuFoodDto::from)
                .toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = ALL_MENUS_CACHE_VALUE, key = ALL_MENUS_CACHE_KEY, cacheManager = REDIS_CACHE_MANAGER)
    public MenuInfoDto updateMenu(MenuUpdateDto dto, MultipartFile file, Long id, String uploadDir) throws CantFindByIdException, UploadFileException {
        Menu menu = menuRepository.findByIdWithMenuInfo(id).orElseThrow(() ->
                new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id));

        if (file != null) {
            if (menu.getImage() == null) menu.setImage(menuImageService.uploadImage(file, uploadDir));
            else menuImageService.updateImage(file, menu.getImage());
        }
        MenuInfo menuInfo = menu.getMenuInfo();

        menu.patch(dto);
        menuInfo.patch(dto);
        return MenuInfoDto.from(menu);
    }

    @Override
    @Transactional
    @CacheEvict(value = ALL_MENUS_CACHE_VALUE, key = ALL_MENUS_CACHE_KEY, cacheManager = REDIS_CACHE_MANAGER)
    public MenuDto deleteMenu(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id));

        userPolicyRepository.saveAll(userPolicyRepository.findAllByDefaultMenu(id).stream()
                .peek(UserPolicy::clearDefaultMenu)
                .toList());
        menuRepository.delete(menu);
        CompletableFuture.runAsync(() -> deleteOrders(menu.getName()));

        return MenuDto.from(menu);
    }

    @Transactional
    public void deleteOrders(String menuName) {
        orderRepository.deleteAllByMenu(menuName);
    }

    @Override
    @Transactional
    @CacheEvict(value = ALL_MENUS_CACHE_VALUE, key = ALL_MENUS_CACHE_KEY, cacheManager = REDIS_CACHE_MANAGER)
    public MenuDto setSoldOut(Long id, String type) throws CantFindByIdException, WrongParameter {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id));

        switch (type) {
            case "n" -> menu.setSoldFalse();
            case "y" -> menu.setSoldTrue();
            default -> throw new WrongParameter(type);
        }
        return MenuDto.from(menu);
    }

    @Override
    @Transactional
    public MenuDto setPlanner(Long id) throws CantFindByIdException, WrongParameter {
        if (menuRepository.existsByUsePlannerTrue()) throw new WrongParameter("""
                이미 식단표를 사용하고 있습니다.
                id의 메뉴 정보를 수정할 수 없습니다.
                id : """ + id);

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id));
        menu.usePlanner();
        return MenuDto.from(menu);
    }

    @Override
    @Transactional
    public MenuDto changePlanner(Long id) throws CantFindByIdException {
        menuRepository.findByUsePlanner(true).ifPresent(Menu::notUsePlanner);

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id));
        menu.usePlanner();
        return MenuDto.from(menu);
    }

    @Override
    public MenuFoodDto addFood(String name, Map<String, Integer> data) throws JsonProcessingException {
        return MenuFoodDto.from(menuFoodRepository.save(MenuFood.of(name, objectMapper.writeValueAsString(data))));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuFoodDto getOneFood(Long id) throws CantFindByIdException {
        return MenuFoodDto.from(menuFoodRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 Menu를 찾을 수 없습니다.
                menuId : """, id)));
    }
}
