package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.calculate.CalculateDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final StoreStateRepository storeStateRepository;

    private final Long finalId = 1L;

    private String getTodayDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    public StoreDto isSetting() {
        Store store = storeRepository.findById(finalId).orElse(null);
        if (store == null) return null;
        return StoreDto.createStoreDto(store);
    }

    public StoreDto getStoreData() throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        return StoreDto.createStoreDto(store);
    }


    public void setSetting(StoreDto dto) throws SameNameException {
        if (storeRepository.existsById(finalId)) {
            throw new SameNameException("중복된 요청입니다.");
        }
        Store store = Store.createStore(finalId, dto);
        storeRepository.save(store);
    }

    @Transactional
    public Integer getToday() throws JsonProcessingException {
        Calculate calculate = calculateRepository.findByDate(getTodayDate());
        if (calculate == null) return null;
        Integer result = calculateMenuRepository.sumCountByCalculateId(calculate.getId());
        return result;
    }

    @Transactional
    public List<StoreWeekendDto> getWeekend() {
        List<Calculate> calculateList = calculateRepository.findTop5ByOrderByCreatedAtDesc();
        List<StoreWeekendDto> result = new ArrayList<>();

        for (Calculate c : calculateList) {
            result.add(new StoreWeekendDto(c.getDate(), calculateMenuRepository.sumCountByCalculateId(c.getId())));
        }

        return result;
    }

    public int getAllUsers() {
        Integer result = calculateMenuRepository.getAllUsers();
        return (result != null) ? result : 0;
    }

    @Transactional
    public StoreDto updateStoreInfo(StoreDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        store.setInfo(dto);

        return StoreDto.createStoreDto(store);
    }

    @Transactional
    public void setOff(Boolean off, int year, int month, int day) {
        String dateString = DateTools.makeDateString(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(dateString);
        if (storeState == null) {
            StoreState state = new StoreState(null, storeRepository.getReferenceById(finalId), dateString, off);
            storeStateRepository.save(state);
        }
        else {
            storeState.setOff(off);
        }
    }

    public List<StoreStateDto> getOffOfMonth(int year, int month) throws WrongParameter {
        if (0 >= month || month > 12) throw new WrongParameter("잘못된 입력입니다.");

        String start = DateTools.makeDateString(year, month, 1);
        String end = DateTools.makeDateString(year, month, DateTools.getLastDay(year, month));

        List<StoreState> result = storeStateRepository.findAllByDateBetween(start, end);
        return result.stream()
                .map(state -> StoreStateDto.of(state))
                .collect(Collectors.toList());
    }
}
