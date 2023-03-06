package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverShowDto;
import com.hanbat.zanbanzero.entity.leftover.LeftoverHistory;
import com.hanbat.zanbanzero.entity.leftover.LeftoverShow;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.repository.leftover.LeftoverHistoryRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final LeftoverRepository leftoverRepository;
    private final LeftoverHistoryRepository leftoverHistoryRepository;

    private Long storeId = 1L;

    public LeftoverShowDto getLeftover() throws CantFindByIdException {
        LeftoverShow result = leftoverRepository.findById(storeId).orElseThrow(CantFindByIdException::new);
        return LeftoverShowDto.createLeftoverShowDto(result);
    }

    @Transactional
    public void setLeftover(LeftoverShowDto leftoverShowDto) throws CantFindByIdException {
        LeftoverShow old = leftoverRepository.findById(storeId).orElseThrow(CantFindByIdException::new);
        LeftoverHistory history = LeftoverHistory.createLeftoverHistory(old);

        old.update(leftoverShowDto.getLeftover());

        leftoverHistoryRepository.save(history);
    }

    public List<LeftoverHistoryDto> getAllLeftover(Long count) {
        List<LeftoverHistory> result = leftoverHistoryRepository.getAllLeftoverCount(count);

        return result.stream()
                .map((history) -> LeftoverHistoryDto.createLeftoverHistoryDto(history))
                .collect(Collectors.toList());
    }
}
