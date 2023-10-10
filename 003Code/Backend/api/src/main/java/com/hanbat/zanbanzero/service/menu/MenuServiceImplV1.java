package com.hanbat.zanbanzero.service.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.menu.*;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.exception.exceptions.*;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuServiceImplV1 implements MenuService{

    private final ImageService menuImageService;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final MenuInfoRepository menuInfoRepository;
    private final MenuFoodRepository menuFoodRepository;

    private static final String MENU_DTO_CACHE_KEY = "1";
    private static final String CACHE_MANAGER = "cacheManager";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Cacheable(value = "MenuUserInfoDtos", key = MENU_DTO_CACHE_KEY, cacheManager = CACHE_MANAGER)
    public MenuUserInfoDtos getMenus() {
        return MenuUserInfoDtos.of(menuRepository.findAllWithMenuInfo().stream()
                .map(MenuUserInfoDto::of)
                .toList());
    }

    @Override
    @Cacheable(value = "MenuInfoDto", key = "#id", cacheManager = CACHE_MANAGER)
    public MenuInfoDto getMenuInfo(Long id) throws CantFindByIdException {
        return MenuInfoDto.of(menuInfoRepository.findByIdAndFetch(id).orElseThrow(() -> new CantFindByIdException("id : " + id)));
    }

    @Override
    @Transactional
    public List<MenuManagerInfoDto> getMenusForManager() {
        return menuRepository.findAllWithMenuInfo().stream()
                .map(MenuManagerInfoDto::of)
                .toList();
    }

    @Override
    public Boolean isPlanned() { return menuRepository.existsByUsePlannerTrue(); }

    @Override
    @Transactional
    public MenuDto addMenu(MenuUpdateDto dto, String filePath) throws SameNameException {
        if (menuRepository.existsByName(dto.getName()) || (menuRepository.existsByUsePlannerTrue() && dto.getUsePlanner())) throw new SameNameException("dto : " + dto);

        Menu menu = menuRepository.save(Menu.of(dto, filePath));
        menuInfoRepository.save(dto.of(menu));
        return MenuDto.of(menu);
    }

    @Override
    @Transactional
    public MenuFoodDto addFood(Long id, String data) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        return MenuFoodDto.of(menuFoodRepository.save(MenuFood.of(menu, data)));
    }

    @Override
    public Map<String, Integer> getFood(Long id) throws MapToStringException {
        MenuFood result = menuFoodRepository.findById(id).orElse(null);
        if (result == null) return Collections.emptyMap();
        try {
            return objectMapper.readValue(result.getFood(), Map.class);
        } catch (JsonProcessingException e) {
            throw new MapToStringException("Food : " + result.getFood() ,e);
        }
    }

    @Override
    @Transactional
    public Map<String, Integer> updateFood(Long id, Map<String, Integer> map) throws CantFindByIdException, MapToStringException {
        MenuFood menuFood = menuFoodRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        try {
            menuFood.setFood(objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            throw new MapToStringException("FoodMap : " + map ,e);
        }
        return map;
    }

    @Override
    @Transactional
    @CachePut(value = "MenuInfoDto", key = "#id", cacheManager = CACHE_MANAGER)
    @CacheEvict(value = "MenuUserInfoDtos", key = MENU_DTO_CACHE_KEY, cacheManager = CACHE_MANAGER)
    public MenuInfoDto updateMenu(MenuUpdateDto dto, MultipartFile file, Long id, String uploadDir) throws CantFindByIdException, UploadFileException {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        MenuInfo menuInfo = menuInfoRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        if (file != null) {
            if (menu.getImage() == null) menu.setImage(menuImageService.uploadImage(file, uploadDir));
            else menuImageService.updateImage(file, menu.getImage());
        }

        menu.patch(dto);
        menuInfo.patch(dto);
        return MenuInfoDto.of(menuInfo);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "MenuInfoDto", key = "#id", cacheManager = CACHE_MANAGER),
            @CacheEvict(value = "MenuUserInfoDtos", key = MENU_DTO_CACHE_KEY, cacheManager = CACHE_MANAGER)
    })
    public MenuDto deleteMenu(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        userPolicyRepository.saveAll(userPolicyRepository.findAllByDefaultMenu(id).stream()
                .peek(policy -> policy.setDefaultMenu(null))
                .toList());
        menuRepository.delete(menu);

        return MenuDto.of(menu);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "MenuInfoDto", key = "#id", cacheManager = CACHE_MANAGER),
            @CacheEvict(value = "MenuUserInfoDtos", key = MENU_DTO_CACHE_KEY, cacheManager = CACHE_MANAGER)
    })
    public MenuDto setSoldOut(Long id, String type) throws CantFindByIdException, WrongParameter {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        switch (type) {
            case "n" -> menu.setSold(false);
            case "y" -> menu.setSold(true);
            default -> throw new WrongParameter(type);
        }
        return MenuDto.of(menu);
    }

    @Override
    @Transactional
    public MenuDto setPlanner(Long id) throws CantFindByIdException, WrongParameter {
        if (menuRepository.existsByUsePlannerTrue()) throw new WrongParameter("이미 식단표를 사용하고 있습니다. id : " + id);

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        menu.setUsePlanner(true);
        return MenuDto.of(menu);
    }

    @Override
    @Transactional
    public MenuDto changePlanner(Long id) throws CantFindByIdException {
        Menu old = menuRepository.findByUsePlanner(true);
        if (old != null) old.setUsePlanner(false);

        Menu menu = menuRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        menu.setUsePlanner(true);
        return MenuDto.of(menu);
    }
}
