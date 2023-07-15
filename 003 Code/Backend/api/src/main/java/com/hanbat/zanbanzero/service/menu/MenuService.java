package com.hanbat.zanbanzero.service.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.menu.MenuManagerInfoDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.dto.menu.MenuInfoDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.entity.menu.MenuFood;
import com.hanbat.zanbanzero.entity.menu.MenuInfo;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.menu.MenuFoodRepository;
import com.hanbat.zanbanzero.repository.menu.MenuInfoRepository;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ImageService menuImageService;
    private final UserPolicyRepository userPolicyRepository;
    private final MenuRepository menuRepository;
    private final MenuInfoRepository menuInfoRepository;
    private final MenuFoodRepository menuFoodRepository;

    private final String menuCacheKey = "1";
    private final String cacheManager = "cacheManager";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "MenuDto", key = menuCacheKey, cacheManager = cacheManager)
    public List<MenuDto> getMenus() {
        return menuRepository.findAll().stream()
                .map((menu) -> MenuDto.of(menu))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "MenuInfoDto", key = "#id", cacheManager = cacheManager)
    public MenuInfoDto getMenuInfo(Long id) throws CantFindByIdException {
        return MenuInfoDto.of(menuInfoRepository.findByIdAndFetch(id).orElseThrow(CantFindByIdException::new));
    }

    @Transactional
    public List<MenuManagerInfoDto> getMenusForManager() {
        return menuRepository.findAllWithMenuInfo().stream()
                .map(menu -> MenuManagerInfoDto.of(menu))
                .collect(Collectors.toList());
    }

    public Boolean isPlanned() { return menuRepository.existsByUsePlannerTrue(); }

    @Transactional
    @CacheEvict(value = "MenuDto", key = menuCacheKey, cacheManager = cacheManager)
    public MenuDto addMenu(MenuUpdateDto dto, String filePath) throws SameNameException {
        if (menuRepository.existsByName(dto.getName()) || (menuRepository.existsByUsePlannerTrue() && dto.getUsePlanner())) throw new SameNameException("데이터 중복입니다.");

        Menu menu = menuRepository.save(Menu.of(dto, filePath));
        menuInfoRepository.save(dto.of(menu));
        return MenuDto.of(menu);
    }

    @Transactional
    public void addFood(Long id, String data) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        menuFoodRepository.save(MenuFood.of(menu, data));
    }

    public Map<String, Integer> getFood(Long id) throws CantFindByIdException, JsonProcessingException {
        String result = menuFoodRepository.findById(id).orElseThrow(CantFindByIdException::new).getFood();
        return objectMapper.readValue(result, Map.class);
    }

    @Transactional
    public Map<String, Integer> updateFood(Long id, Map<String, Integer> map) throws CantFindByIdException, JsonProcessingException {
        MenuFood menuFood = menuFoodRepository.findById(id).orElseThrow(CantFindByIdException::new);
        Map<String, Integer> old = objectMapper.readValue(menuFood.getFood(), Map.class);
        old.putAll(map);

        menuFood.setFood(objectMapper.writeValueAsString(old));
        return old;
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = menuCacheKey, cacheManager = cacheManager)
    public void updateMenu(MenuUpdateDto dto, MultipartFile file, Long id, String uploadDir) throws CantFindByIdException, IOException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);
        MenuInfo menuInfo = menuInfoRepository.findById(id).orElseThrow(CantFindByIdException::new);

        if (file != null) {
            if (menu.getImage() == null) menu.setImage(menuImageService.uploadImage(file, uploadDir));
            else menuImageService.updateImage(file, menu.getImage());
        }

        menu.patch(dto);
        menuInfo.patch(dto);
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = menuCacheKey, cacheManager = cacheManager)
    public void deleteMenu(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        userPolicyRepository.saveAll(userPolicyRepository.findAllByDefaultMenu(id).stream()
                .peek(policy -> policy.setDefaultMenu(null))
                .collect(Collectors.toList()));
        menuRepository.delete(menu);
    }

    @Transactional
    @CacheEvict(value = "MenuDto", key = menuCacheKey, cacheManager = cacheManager)
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
