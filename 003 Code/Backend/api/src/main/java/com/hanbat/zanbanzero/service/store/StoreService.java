package com.hanbat.zanbanzero.service.store;

import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.entity.calculate.CalculateMenu;
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
    private int dataSize = 5;

    public StoreDto isSetting() {
        Store store = storeRepository.findById(finalId).orElse(null);
        if (store == null) return null;
        return StoreDto.createStoreDto(store);
    }

    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.createStoreDto(storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new));
    }

    public void setSetting(StoreDto dto) throws SameNameException {
        if (storeRepository.existsById(finalId)) throw new SameNameException("중복된 요청입니다.");

        Store store = Store.createStore(finalId, dto);
        storeRepository.save(store);
    }

    @Transactional
    public Integer getToday() {
        Calculate calculate = calculateRepository.findByDate(DateTools.makeTodayDateString());
        if (calculate == null) return 0;
        return calculate.getToday();
    }

    @Transactional
    public List<StoreWeekendDto> getLastWeeksUser() throws WrongParameter {
        List<StoreWeekendDto> result = new ArrayList<>();
        LocalDate date = DateTools.getLastWeeksMonday(0);

        for (int i = 0; i < dataSize; i ++) {
            String targetDate = DateTools.toFormatterString(date.plusDays(i));
            Calculate calculate = calculateRepository.findByDate(targetDate);

            if (calculate == null) result.add(StoreWeekendDto.createZeroStoreWeekendDto(targetDate));
            else result.add(StoreWeekendDto.createStoreWeekendDto(targetDate, calculate.getToday()));
        }

        return result;
    }

    public int getAllUsers() {
        Integer result = calculateMenuRepository.getAllUsers();
        return (result != null) ? result : 0;
    }

    @Transactional
    public List<CalculateMenuForGraphDto> getPopularMenus() {
        List<Long> idList = calculateRepository.findTop5ByIdOrderByIdDesc()
                .stream()
                .map(calculate -> calculate.getId())
                .collect(Collectors.toList());

        return calculateMenuRepository.getPopularMenus(idList);
    }

    @Transactional
    public StoreDto updateStoreTitle(StoreDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        store.setName(dto);

        return StoreDto.createStoreDto(store);
    }

    @Transactional
    public StoreDto updateStoreInfo(StoreDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        store.setInfo(dto);

        return StoreDto.createStoreDto(store);
    }

    @Transactional
    public void setOff(Boolean off, int year, int month, int day) {
        String dateString = DateTools.makeResponseDateFormatString(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(dateString);
        if (storeState == null) storeStateRepository.save(StoreState.createNewOffStoreState(storeRepository.getReferenceById(finalId), dateString));
        else storeState.setOff(off);
    }

    public List<StoreStateDto> getClosedDays(int year, int month) throws WrongParameter {
        String start = DateTools.makeResponseDateFormatString(year, month, 1);
        String end = DateTools.makeResponseDateFormatString(year, month, DateTools.getLastDay(year, month));

        return storeStateRepository.findAllByDateBetween(start, end).stream()
                .map(state -> StoreStateDto.of(state))
                .collect(Collectors.toList());
    }
}
