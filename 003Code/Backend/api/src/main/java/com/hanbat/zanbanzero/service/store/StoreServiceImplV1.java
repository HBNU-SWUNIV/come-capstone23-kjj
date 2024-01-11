package com.hanbat.zanbanzero.service.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.calculate.CalculatePreWeekDto;
import com.hanbat.zanbanzero.dto.sbiz.WeeklyFoodPredictDto;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.entity.batch.BatchDate;
import com.hanbat.zanbanzero.entity.batch.calculate.Calculate;
import com.hanbat.zanbanzero.entity.batch.calculate.CalculateMenu;
import com.hanbat.zanbanzero.entity.batch.calculate.CalculatePre;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.StringToMapException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import com.hanbat.zanbanzero.repository.batch.BatchDateRepository;
import com.hanbat.zanbanzero.repository.batch.calculate.CalculateMenuRepository;
import com.hanbat.zanbanzero.repository.batch.calculate.CalculatePreRepository;
import com.hanbat.zanbanzero.repository.batch.calculate.CalculatePreWeekRepository;
import com.hanbat.zanbanzero.repository.batch.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.batch.predict.WeeklyFoodPredictRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.service.DateUtil;
import com.hanbat.zanbanzero.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static com.hanbat.zanbanzero.entity.store.Store.FINAL_STORE_ID;


@Service
@RequiredArgsConstructor
public class StoreServiceImplV1 implements StoreService {

    private final ImageService imageService;
    private final StoreRepository storeRepository;
    private final CalculateRepository calculateRepository;
    private final CalculateMenuRepository calculateMenuRepository;
    private final CalculatePreWeekRepository calculatePreWeekRepository;
    private final BatchDateRepository batchDateRepository;
    private final CalculatePreRepository calculatePreRepository;
    private final StoreStateRepository storeStateRepository;
    private final WeeklyFoodPredictRepository weeklyFoodPredictRepository;
    private final DateUtil dateUtil;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(readOnly = true)
    public StoreDto isSetting() {
        Store store = storeRepository.findById(FINAL_STORE_ID).orElse(null);
        if (store == null) return null;
        return StoreDto.from(store);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreDto getStoreData() throws CantFindByIdException {
        return StoreDto.from(storeRepository.findById(FINAL_STORE_ID).orElseThrow(() -> new CantFindByIdException("""
                ID가 1인 Store 데이터가 존재하지 않습니다.
                존재 여부를 확인해주세요.
                storeId : """, FINAL_STORE_ID)));
    }

    @Override
    @Transactional
    public StoreDto setSetting(StoreSettingDto dto) throws SameNameException {
        if (storeRepository.existsById(FINAL_STORE_ID)) throw new SameNameException("""
                이미 ID가 1인 Store 데이터가 존재합니다.
                dto : """ + dto);

        return StoreDto.from(storeRepository.save(Store.of(FINAL_STORE_ID, dto)));
    }

    @Override
    @Transactional
    public void setStoreImage(MultipartFile file) throws CantFindByIdException, UploadFileException {
        Store store = storeRepository.findById(FINAL_STORE_ID).orElseThrow(() -> new CantFindByIdException("""
                ID가 1인 Store 데이터가 존재하지 않습니다.
                존재 여부를 확인해주세요.
                storeId : """, FINAL_STORE_ID));
        String uploadDir = "img/store";
        if (store.getImage() == null) store.setImage(imageService.uploadImage(file, uploadDir));
        else imageService.updateImage(file, store.getImage());
    }

    @Override
    @Transactional(readOnly = true)
    public StoreTodayDto getToday() {
        return StoreTodayDto.from(calculateRepository.findLastTwoToday());
    }

    @Override
    @Transactional(readOnly = true)
    public StoreSalesDto getSales() {
        return StoreSalesDto.from(calculateRepository.findLastTwoSales());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreWeekendDto> getLastWeeksUser() {
        List<StoreWeekendDto> result = new ArrayList<>();
        LocalDate date = dateUtil.getLastWeeksMonday(0);

        int dataSize = 5;
        for (int i = 0; i < dataSize; i ++) {
            LocalDate targetDate = date.plusDays(i);
            BatchDate batchDate = batchDateRepository.findByDate(targetDate).orElse(null);

            if (batchDate == null) result.add(StoreWeekendDto.newZeroDataStoreWeekendDto(targetDate));
            else {
                Calculate calculate = calculateRepository.findByBatchDate(batchDate).orElse(Calculate.createZeroCalculateData());
                result.add(StoreWeekendDto.of(targetDate, calculate.getToday()));
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int getAllUsers() {
        Integer result = calculateMenuRepository.getAllUsers();
        return (result != null) ? result : 0;
    }

    private Map<String, Integer> getMenuCountMap(List<Long> idList) {
        Map<String, Integer> result = new HashMap<>();

        List<CalculateMenu> calculateMenus = calculateMenuRepository.getPopularMenus(idList);
        calculateMenus.forEach(calculateMenu -> {
            String name = calculateMenu.getMenu();
            Integer count = calculateMenu.getCount();
            result.put(name, result.getOrDefault(name, 0) + count);
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalculateMenuForGraphDto> getPopularMenus() {
        // 최근 5영업일 정산 데이터 id 조회
        List<Long> idList = calculateRepository.findTop5IdOrderByIdDesc();

        Map<String, Integer> menuCountMap = getMenuCountMap(idList);
        // maxHeap 생성
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
                (entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue())
        );
        maxHeap.addAll(menuCountMap.entrySet());

        // result 생성
        List<CalculateMenuForGraphDto> result = new ArrayList<>();
        int size = 3;
        while (size > 0 && !maxHeap.isEmpty()) {
            Map.Entry<String, Integer> poll = maxHeap.poll();
            result.add(CalculateMenuForGraphDto.from(poll.getKey(), poll.getValue()));
            size--;
        }
        return result;
    }

    @Override
    @Transactional
    public StoreDto updateStoreTitle(StoreTitleDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_STORE_ID).orElseThrow(() -> new CantFindByIdException("""
                ID가 1인 Store 데이터가 존재하지 않습니다.
                존재 여부를 확인해주세요.
                storeId : """, FINAL_STORE_ID));
        store.setName(dto.name());

        return StoreDto.from(store);
    }

    @Override
    @Transactional
    public StoreDto updateStoreInfo(StoreInfoDto dto) throws CantFindByIdException {
        Store store = storeRepository.findById(FINAL_STORE_ID).orElseThrow(() -> new CantFindByIdException("""
                ID가 1인 Store 데이터가 존재하지 않습니다.
                존재 여부를 확인해주세요.
                storeId : """, FINAL_STORE_ID));
        store.setInfo(dto.info());

        return StoreDto.from(store);
    }

    @Override
    @Transactional
    public StoreStateDto setOff(StoreOffDto dto, int year, int month, int day) {
        LocalDate date = dateUtil.makeLocalDate(year, month, day);

        StoreState storeState = storeStateRepository.findByDate(date).orElse(null);
        if (storeState == null) {
            StoreState newData = StoreState.createNewOffStoreState(storeRepository.getReferenceById(FINAL_STORE_ID), date, dto);
            storeState = storeStateRepository.save(newData);
        }
        else storeState.setOff(dto);
        return StoreStateDto.from(storeState);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreStateDto> getClosedDays(int year, int month) {
        LocalDate start = dateUtil.makeLocalDate(year, month, 1);
        LocalDate end = dateUtil.makeLocalDate(year, month, dateUtil.getLastDay(year, month));

        return storeStateRepository.findAllByDateBetween(start, end).stream()
                .map(StoreStateDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StorePreDto getCalculatePreUser() {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();
        Calculate calculate = calculateRepository.findTopByOrderByIdDesc().orElse(Calculate.createZeroCalculateData());

        return StorePreDto.from(List.of(calculate.getToday(), calculatePre.getPredictUser()));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getCalculatePreFood() throws StringToMapException, CantFindByIdException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        try {
            return objectMapper.readValue(calculatePre.getPredictFood(), HashMap.class);
        } catch (JsonProcessingException e) {
            throw new StringToMapException("""
                    calculatePre는 존재하지만, 
                    PredictMenu 필드를 HashMap으로 변환하는 과정에서 에러가 발생했습니다.
                    PredictFood : """ + calculatePre.getPredictFood() ,e);
        } catch (NullPointerException e) {
            throw new CantFindByIdException("""
                    calculatePre가 null 입니다.
                    데이터가 존재하는지 확인해주세요.
                    """, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getCalculatePreMenu() throws StringToMapException, CantFindByIdException {
        CalculatePre calculatePre = calculatePreRepository.findTopByOrderByIdDesc();

        try {
            return objectMapper.readValue(calculatePre.getPredictMenu(), HashMap.class);
        } catch (JsonProcessingException e) {
            throw new StringToMapException("""
                    calculatePre는 존재하지만, 
                    PredictMenu 필드를 HashMap으로 변환하는 과정에서 에러가 발생했습니다.
                    PredictFood : """ + calculatePre.getPredictFood() ,e);
        } catch (NullPointerException e) {
            throw new CantFindByIdException("""
                    calculatePre가 null 입니다.
                    데이터가 존재하는지 확인해주세요.
                    """, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CalculatePreWeekDto getNextWeeksUser() {
        return CalculatePreWeekDto.from(calculatePreWeekRepository.findFirstByOrderByIdDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public WeeklyFoodPredictDto getNextWeeksFood() {
        return WeeklyFoodPredictDto.from(weeklyFoodPredictRepository.findFirstByOrderByIdDesc());
    }
}
