package com.hanbat.zanbanzero.service.store;

import com.hanbat.zanbanzero.dto.store.StoreStateDto;
import com.hanbat.zanbanzero.entity.store.Store;
import com.hanbat.zanbanzero.entity.store.StoreState;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.RequestDataisNull;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.repository.store.StoreStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreStateRepository storeStateRepository;

    private Long storeId = 1L;

    @Transactional
    public void setLocation(Long lat, Long lon) throws CantFindByIdException, RequestDataisNull {
        if (lat == null || lon == null) {
            throw new RequestDataisNull("데이터가 부족합니다.");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(CantFindByIdException::new);
        store.setLocation(lat, lon);
    }

    public Map<String, Long> getLocation() throws CantFindByIdException {
        Store store = storeRepository.findById(storeId).orElseThrow(CantFindByIdException::new);

        return Map.of(
                "lat", store.getLat(),
                "lon", store.getLon()
        );
    }

    @Transactional
    public void setCongestion(StoreStateDto storeStateDto) throws CantFindByIdException {
        StoreState storeState = storeStateRepository.findById(storeId).orElseThrow(CantFindByIdException::new);
        storeState.setCongestion(storeStateDto.getCongestion());
    }

    public Long getCongestion() throws CantFindByIdException {
        StoreState storeState = storeStateRepository.findById(storeId).orElseThrow(CantFindByIdException::new);

        return storeState.getCongestion();
    }
}
