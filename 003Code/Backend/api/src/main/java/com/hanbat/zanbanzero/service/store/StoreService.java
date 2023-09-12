package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.calculate.CalculatePre;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.repository.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculatePreRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.service.DateTools;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final ImageService imageService;
    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final CalculatePreRepository calculatePreRepository;
    private final StoreStateRepository storeStateRepository;

    private static final Long FINAL_ID = 1L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StoreDto isSetting() {
        Store store = storeRepository.findById(FINAL_ID).orElse(null);
        if (store == null) return null;
        return StoreDto.of(store);
    }

    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.of(storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new));
    }

    @Transactional
    public StoreDto setSetting(StoreSettingDto dto) throws SameNameException {
        if (storeRepository.existsById(FINAL_ID)) throw new SameNameException("dto : " + dto);

        return StoreDto.of(storeRepository.save(Store.of(FINAL_ID, dto)));
    }

    @Transactional
    public void setStoreImage(MultipartFile file) throws CantFindByIdException, IOException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        String uploadDir = "img/store";
        if (store.getImage() == null) store.setImage(imageService.uploadImage(file, uploadDir));
        else imageService.updateImage(file, store.getImage());
    }

    @Transactional
    public Integer getToday() {
        Calculate calculate = calculateRepository.findByDate(DateTools.makeTodayToLocalDate());
        if (calculate == null) return 0;
        return calculate.getToday();
    }

    @Transactional
    public List<StoreWeekendDto> getLastWeeksUser() {
        List<StoreWeekendDto> result = new ArrayList<>();
        LocalDate date = DateTools.getLastWeeksMonday(0);

        int dataSize = 5;
        for (int i = 0; i < dataSize; i ++) {
            LocalDate targetDate = date.plusDays(i);
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
        List<Long> idList = calculateRepository.findTop5ByIdOrderByIdDesc().stream()
                .map(Calculate::getId)
                .toList();

        List<CalculateMenuForGraphDto> result = calculateMenuRepository.getPopularMenus(idList);
        result.sort(Comparator.comparingLong(CalculateMenuForGraphDto::getCount).reversed());

        if (result.size() < 3) return result;
        return result.subList(0, 3);
    }

    @Transactional
    public StoreDto updateStoreTitle(StoreTitleDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        store.setName(dto.getName());

        return StoreDto.of(store);
    }

    @Transactional
    public StoreDto updateStoreInfo(StoreInfoDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        store.setInfo(dto.getInfo());

        return StoreDto.of(store);
    }

    @Transactional
    public StoreStateDto setOff(Boolean off, int year, int month, int day) {
        LocalDate date = DateTools.makeLocalDate(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(date);
        if (storeState == null) storeState = storeStateRepository.save(StoreState.createNewOffStoreState(storeRepository.getReferenceById(FINAL_ID), date, off));
        else storeState.setOff(off);
        return StoreStateDto.of(storeState);
    }

    public List<StoreStateDto> getClosedDays(int year, int month) {
        LocalDate start = DateTools.makeLocalDate(year, month, 1);
        LocalDate end = DateTools.makeLocalDate(year, month, DateTools.getLastDay(year, month));

        return storeStateRepository.findAllByDateBetween(start, end).stream()
                .map(StoreStateDto::of)
                .toList();
    }

    @Transactional
    public Integer getCalculatePreUser() {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        return calculatePre.getPredictUser();
    }

    @Transactional
    public Map<String, Integer> getCalculatePreFood() throws JsonProcessingException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        return objectMapper.readValue(calculatePre.getPredictFood(), Map.class);
    }

    @Transactional
    public Map<String, Integer> getCalculatePreMenu() throws JsonProcessingException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        return objectMapper.readValue(calculatePre.getPredictMenu(), Map.class);
    }
}
