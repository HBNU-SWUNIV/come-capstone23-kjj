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
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final ImageService imageService;
    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final CalculatePreRepository calculatePreRepository;
    private final StoreStateRepository storeStateRepository;

    private final Long finalId = 1L;
    private String uploadDir = "img/store";

    private ObjectMapper objectMapper = new ObjectMapper();

    public StoreDto isSetting() {
        Store store = storeRepository.findById(finalId).orElse(null);
        if (store == null) return null;
        return StoreDto.of(store);
    }

    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.of(storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new));
    }

    @Transactional
    public StoreDto setSetting(StoreDto dto) throws SameNameException {
        if (storeRepository.existsById(finalId)) throw new SameNameException("중복된 요청입니다.");

        return StoreDto.of(storeRepository.save(Store.of(finalId, dto)));
    }

    @Transactional
    public void setStoreImage(MultipartFile file) throws CantFindByIdException, IOException {
        Store store = storeRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        if (store.getImage() == null) store.setImage(imageService.uploadImage(file, uploadDir));
        else imageService.updateImage(file, store.getImage());
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
    public StoreStateDto setOff(Boolean off, int year, int month, int day) {
        LocalDate date = DateTools.makeDateFormatLocalDate(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(date);
        if (storeState == null) storeState = storeStateRepository.save(StoreState.createNewOffStoreState(storeRepository.getReferenceById(finalId), date));
        else storeState.setOff(off);
        return StoreStateDto.of(storeState);
    }

    public List<StoreStateDto> getClosedDays(int year, int month) {
        LocalDate start = DateTools.makeDateFormatLocalDate(year, month, 1);
        LocalDate end = DateTools.makeDateFormatLocalDate(year, month, DateTools.getLastDay(year, month));

        return storeStateRepository.findAllByDateBetween(start, end).stream()
                .map(state -> StoreStateDto.of(state))
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
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        return objectMapper.readValue(calculatePre.getPredictFood(), Map.class);
    }

    @Transactional
    public Map<String, Integer> getCalculatePreMenu() throws JsonProcessingException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        return objectMapper.readValue(calculatePre.getPredictMenu(), Map.class);
    }
}
