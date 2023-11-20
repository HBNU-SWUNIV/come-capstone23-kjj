package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.repository.leftover.LeftoverPreRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverRepository;
import com.hanbat.zanbanzero.service.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftoverServiceImplV1 implements LeftoverService{

    private final LeftoverRepository leftoverRepository;
    private final LeftoverPreRepository leftoverPreRepository;
    private final DateUtil dateUtil;

    private static final int DATA_SIZE = 5;

    @Override
    @Transactional(readOnly = true)
    public List<LeftoverDto> getLastWeeksLeftovers(int type) {
        List<LeftoverDto> result = new ArrayList<>();
        LocalDate date = dateUtil.getLastWeeksMonday(type);

        for (int i = 0; i < DATA_SIZE; i ++) {
            LocalDate targetDate = date.plusDays(i);

            LeftoverPre target = leftoverPreRepository.findByDate(targetDate).orElse(null);
            if (target == null) {
                result.add(LeftoverDto.of(targetDate, 0.0));
                continue;
            }

            Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId()).orElse(null);
            if (leftover == null) result.add(LeftoverDto.of(targetDate, 0.0));
            else result.add(LeftoverDto.from(leftover));
        }
        return result;
    }
}
