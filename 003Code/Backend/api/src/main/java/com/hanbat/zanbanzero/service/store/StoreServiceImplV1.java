package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.calculate.CalculatePreWeekDto;
import com.hanbat.zanbanzero.dto.sbiz.WeeklyFoodPredictDto;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.calculate.CalculatePre;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.StringToMapException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import com.hanbat.zanbanzero.repository.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculatePreRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculatePreWeekRepository;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.sbiz.SbizRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.service.DateTools;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class StoreServiceImplV1 implements StoreService {

    private final ImageService imageService;
    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final CalculatePreWeekRepository calculatePreWeekRepository;
    private final CalculatePreRepository calculatePreRepository;
    private final StoreStateRepository storeStateRepository;
    private final SbizRepository sbizRepository;

    private static final Long FINAL_ID = 1L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StoreDto isSetting() {
        Store store = storeRepository.findById(FINAL_ID).orElse(null);
        if (store == null) return null;
        return StoreDto.of(store);
    }

    @Override
    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.of(storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new));
    }

    @Override
    @Transactional
    public StoreDto setSetting(StoreSettingDto dto) throws SameNameException {
        if (storeRepository.existsById(FINAL_ID)) throw new SameNameException("dto : " + dto);

        return StoreDto.of(storeRepository.save(Store.of(FINAL_ID, dto)));
    }

    @Override
    @Transactional
    public void setStoreImage(MultipartFile file) throws CantFindByIdException, UploadFileException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        String uploadDir = "img/store";
        if (store.getImage() == null) store.setImage(imageService.uploadImage(file, uploadDir));
        else imageService.updateImage(file, store.getImage());
    }

    @Override
    public StoreTodayDto getToday() {
        return StoreTodayDto.of(calculateRepository.findLastTwoToday());
    }

    @Override
    public StoreSalesDto getSales() {
        return StoreSalesDto.of(calculateRepository.findLastTwoSales());
    }

    @Override
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

    @Override
    public int getAllUsers() {
        Integer result = calculateMenuRepository.getAllUsers();
        return (result != null) ? result : 0;
    }

    @Override
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

    @Override
    @Transactional
    public StoreDto updateStoreTitle(StoreTitleDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        store.setName(dto.getName());

        return StoreDto.of(store);
    }

    @Override
    @Transactional
    public StoreDto updateStoreInfo(StoreInfoDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_ID).orElseThrow(CantFindByIdException::new);
        store.setInfo(dto.getInfo());

        return StoreDto.of(store);
    }

    @Override
    @Transactional
    public StoreStateDto setOff(StoreOffDto dto, int year, int month, int day) {
        LocalDate date = DateTools.makeLocalDate(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(date);
        if (storeState == null) storeState = storeStateRepository.save(StoreState.createNewOffStoreState(storeRepository.getReferenceById(FINAL_ID), date, dto));
        else storeState.setOff(dto);
        return StoreStateDto.of(storeState);
    }

    @Override
    public List<StoreStateDto> getClosedDays(int year, int month) {
        LocalDate start = DateTools.makeLocalDate(year, month, 1);
        LocalDate end = DateTools.makeLocalDate(year, month, DateTools.getLastDay(year, month));

        return storeStateRepository.findAllByDateBetween(start, end).stream()
                .map(StoreStateDto::of)
                .toList();
    }

    @Override
    @Transactional
    public StorePreDto getCalculatePreUser() {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();
        Calculate calculate = calculateRepository.findTopByOrderByIdDesc();

        return StorePreDto.of(List.of(calculate.getToday(), calculatePre.getPredictUser()));
    }

    @Override
    @Transactional
    public Map<String, Integer> getCalculatePreFood() throws StringToMapException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        try {
            return objectMapper.readValue(calculatePre.getPredictFood(), Map.class);
        } catch (JsonProcessingException e) {
            throw new StringToMapException("PredictFood : " + calculatePre.getPredictFood() ,e);
        }
    }

    @Override
    @Transactional
    public Map<String, Integer> getCalculatePreMenu() throws StringToMapException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        try {
            return objectMapper.readValue(calculatePre.getPredictMenu(), Map.class);
        } catch (JsonProcessingException e) {
            throw new StringToMapException("PredictFood : " + calculatePre.getPredictFood() ,e);
        }
    }

    @Override
    @Transactional
    public CalculatePreWeekDto getNextWeeksUser() {
        return CalculatePreWeekDto.of(calculatePreWeekRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public WeeklyFoodPredictDto getNextWeeksFood() {
        return WeeklyFoodPredictDto.of(sbizRepository.findFirstByOrderByIdDesc());
    }
}
