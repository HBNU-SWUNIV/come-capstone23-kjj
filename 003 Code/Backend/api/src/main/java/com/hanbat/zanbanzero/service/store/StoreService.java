package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.Calculate;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreStateRepository storeStateRepository;
    private final ManagerRepository managerRepository;

    private final Long finalId = 1L;

    private String getTodayDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    public boolean isSetting() {
        boolean result = storeRepository.existsById(finalId);
        return result;
    }

    public StoreDto getStoreData() {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        return StoreDto.createStoreDto(store);
    }


    public void setSetting(StoreDto dto) {
        if (storeRepository.existsById(finalId)) {
            throw new SameNameException("중복된 요청입니다.");
        }
        Store store = Store.createStore(finalId, dto);
        storeRepository.save(store);
    }

    public StoreStateDto getToday() throws JsonProcessingException {
        Calculate calculate = storeStateRepository.findByDate(getTodayDate());
        if (calculate == null) return null;
        return StoreStateDto.createStoreStateDto(calculate);
    }

    public List<StoreWeekendDto> getWeekend() {
        List<Calculate> calculates = storeStateRepository.findTop5ByOrderByCreatedAtDesc();
        return calculates.stream()
                .map(state -> StoreWeekendDto.createStoreWeekendDto(state))
                .collect(Collectors.toList());
    }

    public int getAllUsers() {
        return storeStateRepository.getAllUsers();
    }
}
