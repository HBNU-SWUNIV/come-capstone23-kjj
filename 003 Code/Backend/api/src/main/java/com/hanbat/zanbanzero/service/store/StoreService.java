package com.hanbat.zanbanzero.service.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import com.hanbat.zanbanzero.repository.user.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public void setCongestion(StoreStateDto storeStateDto) throws CantFindByIdException {
        if (storeStateDto.getCongestion() == null) {
            throw new WrongRequestDetails("데이터가 부족합니다.");
        }

        StoreState storeState = storeStateRepository.findById(finalId).orElseThrow(CantFindByIdException::new);
        storeState.setCongestion(storeStateDto.getCongestion());
    }

    public Long getCongestion() throws CantFindByIdException {
        StoreState storeState = storeStateRepository.findById(finalId).orElseThrow(CantFindByIdException::new);

        return storeState.getCongestion();
    }

    public void setSetting(StoreDto dto) {
        if (storeRepository.existsById(finalId)) {
            throw new SameNameException("중복된 요청입니다.");
        }
        Store store = Store.createStore(finalId, managerRepository.getReferenceById(finalId), dto);
        storeRepository.save(store);
    }

    @Transactional
    public void setStoreState() {
        StoreState storeState = new StoreState(null, getTodayDate(), null, 0);
        storeStateRepository.save(storeState);
    }

    public StoreStateDto getToday() {
        StoreState storeState = storeStateRepository.findByDate(getTodayDate());
        if (storeState == null) return null;
        return StoreStateDto.createStoreStateDto(storeState);
    }

    public List<StoreStateDto> getWeekend() {
        List<StoreState> storeStates = storeStateRepository.findTop7ByOrderByCreatedAtDesc();
        return storeStates.stream()
                .map(state -> StoreStateDto.createStoreStateDto(state))
                .collect(Collectors.toList());
    }
}
