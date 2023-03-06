package com.hanbat.zanbanzero.service.menu;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuDto> getMenus() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map((menu) -> MenuDto.createMenuDto(menu))
                .collect(Collectors.toList());
    }

    public MenuDto getMenuInfo(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        return MenuDto.createMenuDto(menu);
    }

    public void addMenu(MenuDto dto) throws SameNameException {
        if (menuRepository.existsByName(dto.getName())) {
            throw new SameNameException("데이터 중복입니다.");
        }

        menuRepository.save(Menu.createMenu(dto));
    }

    @Transactional
    public void updateMenu(MenuDto dto, Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        menu.patch(dto);
    }

    public void deleteMenu(Long id) throws CantFindByIdException {
        Menu menu = menuRepository.findById(id).orElseThrow(CantFindByIdException::new);

        menuRepository.delete(menu);
    }
}
