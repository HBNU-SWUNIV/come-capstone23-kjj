package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.dto.store.StoreWeekendDto;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.calculate.CalculatePre;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.repository.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculatePreRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final CalculatePreRepository calculatePreRepository;
    private final StoreStateRepository storeStateRepository;

    private final Long finalId = 1L;

    public StoreDto isSetting() {
        Store store = storeRepository.findById(finalId).orElse(null);
        if (store == null) return null;
        return StoreDto.of(store);
    }

    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.of(storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new));
    }

    public void setSetting(StoreDto dto) throws SameNameException {
        if (storeRepository.existsById(finalId)) throw new SameNameException("중복된 요청입니다.");

        Store store = Store.of(finalId, dto);
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

        int dataSize = 5;
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
                .map(Calculate::getId)
                .collect(Collectors.toList());

        List<CalculateMenuForGraphDto> result = calculateMenuRepository.getPopularMenus(idList);
        result.sort(Comparator.comparingLong(CalculateMenuForGraphDto::getCount).reversed());

        return result.subList(0, 3);
    }

    @Transactional
    public StoreDto updateStoreTitle(StoreDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        store.setName(dto);

        return StoreDto.of(store);
    }

    @Transactional
    public StoreDto updateStoreInfo(StoreDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        store.setInfo(dto);

        return StoreDto.of(store);
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
                .map(StoreStateDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Integer getCalculatePreUser() {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();
        Integer result = calculatePre.getPredictUser();

        return result;
    }

    @Transactional
    public Map<String, Integer> getCalculatePreFood() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();
        Map<String, Integer> result = objectMapper.readValue(calculatePre.getPredictFood(), Map.class);

        return result;
    }

    @Transactional
    public Map<String, Integer> getCalculatePreMenu() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();
        Map<String, Integer> result = objectMapper.readValue(calculatePre.getPredictMenu(), Map.class);

        return result;
    }
}
