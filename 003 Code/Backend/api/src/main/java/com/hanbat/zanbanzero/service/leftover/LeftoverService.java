package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.entity.leftover.LeftoverHistory;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.leftover.LeftoverHistoryRepository;
import com.hanbat.zanbanzero.repository.store.StoreRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final StoreRepository storeRepository;
    private final LeftoverHistoryRepository leftoverHistoryRepository;

    private Long storeId = 1L;
    private int pageSize = 5;


    @Transactional
    public void setLeftover(LeftoverHistoryDto dto) throws CantFindByIdException {
        if (dto.getLeftover() == null) {
            throw new WrongRequestDetails("데이터가 부족합니다.");
        }

        dto.setDate(DateTools.makeTodayDateString());
        LeftoverHistory target = LeftoverHistory.createLeftoverHistory(dto, storeRepository.getReferenceById(storeId));

        leftoverHistoryRepository.save(target);
    }

    public int getAllLeftoverPage() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<LeftoverHistory> result = leftoverHistoryRepository.findAll(pageable);

        return result.getTotalPages();
    }

    public List<LeftoverHistoryDto> getLeftoverPage(int count) {
        Pageable pageable = PageRequest.of(count, pageSize);
        List<LeftoverHistory> result = leftoverHistoryRepository.findAll(pageable).getContent();

        return result.stream()
                .map((history) -> LeftoverHistoryDto.createLeftoverHistoryDto(history))
                .collect(Collectors.toList());
    }
}
